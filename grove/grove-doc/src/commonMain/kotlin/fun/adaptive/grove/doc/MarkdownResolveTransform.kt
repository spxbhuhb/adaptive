package `fun`.adaptive.grove.doc

import `fun`.adaptive.markdown.model.MarkdownInline
import `fun`.adaptive.markdown.visitor.MarkdownTransformerVoid
import `fun`.adaptive.utility.encodeToUrl
import kotlinx.io.files.Path

class MarkdownResolveTransform(
    val compilation: GroveDocCompilation,
    val mdPath: Path
) : MarkdownTransformerVoid() {

    val inlineLinkRegex = "\\[([^\\[]+)\\]\\(([^)]+)\\)".toRegex()

    override fun visitInline(inline: MarkdownInline): MarkdownInline {
        if (! inline.inlineLink) return inline
        val match = inlineLinkRegex.matchEntire(inline.text) ?: return inline

        val name = match.groupValues[1]
        val url = match.groupValues[2]

        // all specials should be with empty path like [name](scheme://)
        if (! url.endsWith("://")) return inline
        val scheme = url.substringBefore("://")

        return MarkdownInline(
            text = "[$name](${resolve(scheme, name)})",
            bold = inline.bold,
            italic = inline.italic,
            inlineLink = true
        )
    }

    fun resolve(scheme: String, name: String): String =
        when (scheme) {
            "api" -> resolveApi(name)
            "class" -> resolveClass(name)
            "def" -> resolveDef(name)
            "guide" -> resolveGuide(name)
            else -> name
        }

    fun resolveApi(name: String): String =
        pathOrNull(name, compilation.fileCollector.ktFiles) ?: name

    fun resolveClass(name: String): String =
        pathOrNull(name, compilation.fileCollector.ktFiles) ?: name

    fun resolveDef(name: String): String =
        pathOrNull(name, compilation.fileCollector.definitions) ?: name

    fun resolveGuide(name: String): String =
        pathOrNull(name, compilation.fileCollector.guides) ?: name

    fun pathOrNull(name: String, collection: Map<String, List<Path>>): String? {
        val normalizedName = compilation.normalizedName(name)
        val paths = collection[normalizedName] ?: collection[normalizedName.removeSuffix("s")]
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
