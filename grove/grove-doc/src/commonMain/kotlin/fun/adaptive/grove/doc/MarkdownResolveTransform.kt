package `fun`.adaptive.grove.doc

import `fun`.adaptive.file.absolute
import `fun`.adaptive.file.exists
import `fun`.adaptive.file.isDirectory
import `fun`.adaptive.file.list
import `fun`.adaptive.file.readString
import `fun`.adaptive.file.resolve
import `fun`.adaptive.markdown.model.MarkdownCodeFence
import `fun`.adaptive.markdown.model.MarkdownElement
import `fun`.adaptive.markdown.model.MarkdownInline
import `fun`.adaptive.markdown.model.MarkdownParagraph
import `fun`.adaptive.markdown.visitor.MarkdownTransformerVoid
import `fun`.adaptive.utility.*
import kotlinx.io.files.Path

/**
 * Transforms inline links according to the target.
 *
 * AI training data set target transforms:
 *
 * - "def" and "guide" links as-is,
 * - "class", "property", "function" links as-is,
 * - "example" and "dirTree" links into code fences.
 *
 * Human-readable Markdown target transforms:
 *
 * - "def" and "guide" links into MD file names,
 * - "class", "property", "function" links into GitHub links,
 * - "example" and "dirTree" links into code fences.
 */
class MarkdownResolveTransform(
    val compilation: GroveDocCompilation,
    val mdPath: Path,
    val training: Boolean = true
) : MarkdownTransformerVoid() {

    // --------------------------------------------------------------------------------
    // Link parsing
    // --------------------------------------------------------------------------------

    @Suppress("RegExpRedundantEscape") // it is NOT redundant
    val inlineLinkRegex = "\\[([^\\[]+)\\]\\(([^)]+)\\)".toRegex()

    fun parseLink(text: String): Link? {
        val match = inlineLinkRegex.matchEntire(text) ?: return null

        val name = match.groupValues[1]
        val url = match.groupValues[2]

        val scheme = url.substringBefore("://")
        val scope = url.substringAfter("://", "").takeIf { it.isNotEmpty() }

        return Link(name, url, scheme, scope)
    }

    // --------------------------------------------------------------------------------
    // Expand inline links into code fences
    // --------------------------------------------------------------------------------

    override fun visitParagraph(paragraph: MarkdownParagraph): MarkdownElement {
        if (paragraph.children.size != 1) return super.visitParagraph(paragraph)

        val child = paragraph.children.first()
        if (child !is MarkdownInline) return super.visitParagraph(paragraph)
        if (! child.inlineLink) return super.visitParagraph(paragraph)

        val link = parseLink(child.text) ?: return super.visitParagraph(paragraph)

        when (link.scheme) {
            "dirTree" -> replaceDir(link)
            "example" -> replaceExample(link)
            else -> super.visitParagraph(paragraph)
        }
            .also { return it }
    }

    fun replaceExample(link: Link): MarkdownElement {
        val path = link.lookupCode(compilation)
        val content = path?.readString()

        if (content == null && link !in compilation.reportedLinks) {
            compilation.warn("Missing code: $link")
            compilation.reportedLinks.add(link)
        }

        return MarkdownCodeFence(
            language = "kotlin",
            content = path?.readString() ?: "missing code: $link"
        )
    }

    fun replaceDir(link: Link): MarkdownElement {
        val path = link.scope?.let { compilation.inPath.resolve(it) }

        if (path == null && link !in compilation.reportedLinks) {
            compilation.warn("Missing directory: $link")
            compilation.reportedLinks.add(link)
        }

        return MarkdownCodeFence(
            language = "text",
            content = path?.let { dirToText(path) } ?: "missing directory: $link"
        )
    }

    fun dirToText(
        path: Path,
        prefix: String = "",
        builder: StringBuilder = StringBuilder(),
        excludeNames: Set<String> = setOf("build", "kotlin-js-store", ".kotlin", ".gradle")
    ): String {
        if (! path.exists()) return "Directory does not exist"

        if (prefix.isEmpty()) {
            builder.append(path.name).append("\n")
        }

        val files = path.list().sortedWith(compareBy { if (it.isDirectory) "0${it.name}" else "1${it.name}" })

        files.forEachIndexed { index, file ->
            if (excludeNames.contains(file.name)) return@forEachIndexed

            val isLast = index == files.lastIndex
            val currentPrefix = if (isLast) "└── " else "├── "
            val nextPrefix = if (isLast) "    " else "│   "

            builder.append(prefix).append(currentPrefix).append(file.name).append("\n")

            if (file.isDirectory) {
                dirToText(file, prefix + nextPrefix, builder, excludeNames)
            }
        }

        return builder.toString()
    }

    // --------------------------------------------------------------------------------
    // Resolve inline links
    // --------------------------------------------------------------------------------

    override fun visitInline(inline: MarkdownInline): MarkdownInline {
        if (! inline.inlineLink) return inline
        val link = parseLink(inline.text) ?: return inline

        return MarkdownInline(
            text = "[${link.name}](${resolve(link)})",
            bold = inline.bold,
            italic = inline.italic,
            inlineLink = true
        )
    }

    fun resolve(link: Link): String {
        if (training) return link.original

        when (link.scheme) {
            "def" -> resolveDef(link.name)
            "guide" -> resolveGuide(link.name)
            "api" -> resolveClass(link)
            "class" -> resolveClass(link)
            "property" -> resolveClass(link)
            "function" -> resolveClass(link)
            else -> link.original
        }.also {
            return it
        }
    }

    fun resolveClass(link: Link): String {
        val path = link.lookupCode(compilation) ?: return link.name
        val absolutePath = path.absolute().toString().replace('\\', '/')
        val relativePath = absolutePath.removePrefix(compilation.inPathAbsolute)
        return compilation.baseUrl + relativePath
    }

    fun resolveDef(name: String): String =
        pathOrNull(name, compilation.fileCollector.definitions) ?: name

    fun resolveGuide(name: String): String =
        pathOrNull(name, compilation.fileCollector.guides) ?: name

    fun pathOrNull(name: String, collection: Map<String, List<Path>>): String? {
        val normalizedName = compilation.normalizedName(name)

        val paths = collection[normalizedName] ?: collection[PluralHandler.lastWordToSingular(normalizedName)]

        if (paths != null) {
            if (paths.size != 1) {
                compilation.warn("Multiple resolutions for $name, in $mdPath", paths)
            }
            return paths.first().name.encodeToUrl(noPlus = true)
        }

        compilation.warn("No resolution for $name, in $mdPath\"")
        return null
    }

}
