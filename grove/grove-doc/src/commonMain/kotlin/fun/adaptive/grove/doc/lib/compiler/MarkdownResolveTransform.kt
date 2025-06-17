package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.visitor.MarkdownTransformerVoid
import `fun`.adaptive.persistence.*
import `fun`.adaptive.utility.encodeToUrl
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
        val scopeAndArguments = url.substringAfter("://", "").takeIf { it.isNotEmpty() }

        val scope = scopeAndArguments?.substringBefore('?')
        val arguments = scopeAndArguments?.substringAfter('?')

        return Link(name, url, scheme, scope, arguments)
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

            "def" -> {
                if (link.arguments == "inline") {
                    inlineDef(link)
                } else {
                    super.visitParagraph(paragraph)
                }
            }

            else -> super.visitParagraph(paragraph)
        }
            .also { return it }
    }

    fun replaceExample(link: Link): MarkdownElement {
        val path = link.lookupCode(compilation)
        val content = path?.readString()

        var fenceContent: String? = null

        if (content == null && link !in compilation.reportedLinks) {
            compilation.warn("Missing code: $link in $mdPath")
            compilation.reportedLinks.add(link)
        } else {
            if (link.scope != null) {
                fenceContent = content?.let { extractExample(it, link.name) }
                if (fenceContent == null) {
                    // FIXME clean up extract example to recognize classes properly
                    // compilation.warn("Missing example: $link in $mdPath")
                    fenceContent = content
                }
            } else {
                fenceContent = content
            }
        }

        return MarkdownCodeFence(
            language = "kotlin",
            content = fenceContent ?: "missing code: $link in $mdPath"
        )
    }

    fun extractExample(source: String, functionName: String): String? {
        // Match one of the three supported formats
        val regex = Regex(
            """(?://@example|@Adaptive)\s*(suspend\s+)?fun\s+$functionName\s*\([^)]*\)\s*(\{|=\s*runTest\s*\{)""",
            RegexOption.MULTILINE
        )
        val match = regex.find(source) ?: return null

        val startIndex = match.range.first
        var braceCount = 1 // the last char of regex
        var endIndex = match.range.last + 1
        var inCode = false

        while (endIndex < source.length) {
            when (source[endIndex]) {
                '{' -> {
                    braceCount ++
                    inCode = true
                }

                '}' -> {
                    braceCount --
                    if (braceCount == 0 && inCode) {
                        endIndex ++
                        break
                    }
                }
            }
            endIndex ++
        }

        return source.substring(startIndex, endIndex).trim()
    }

    fun inlineDef(link: Link): MarkdownElement {
        val path = link.lookupDef(compilation)
        if (path == null) {
            compilation.warn("Missing definition: $link in $mdPath")
            return MarkdownInline(link.name)
        }

        return extractDef(path.readString())
    }

    fun extractDef(content: String): MarkdownElement {
        val fullAst = MarkdownCompiler.ast(content)

        val title = fullAst.indexOfFirst { it is MarkdownHeader }
        val seeAlso = fullAst.indexOfLast { it is MarkdownHeader }

        val ast = fullAst.subList(title + 1, if (seeAlso != title) seeAlso else fullAst.size)

        val last = ast.last()
        if (last is MarkdownParagraph) { last.closed = true }

        if (ast.size == 1) return ast.first()
        return MarkdownElementGroup(ast.toMutableList())
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
            "def" -> "definition-" + resolveDef(link.name)
            "guide" -> "guide-" + resolveGuide(link.name)
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

        compilation.warn("No resolution for $name, in $mdPath")
        return null
    }

}
