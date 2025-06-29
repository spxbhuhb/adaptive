package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.lib.util.url.Url
import kotlinx.io.files.Path

/**
 * A link in Markdown: `[name](scheme://scope?arguments)
 *
 * @property    original    `scheme://scope?arguments`.
 */
data class Link(
    val name: String,
    val original: String,
    val url : Url
) {
    val scheme: String
        get() = url.scheme

    val scope: String?
        get() = when {
            url.segments.isEmpty() -> null
            url.segments.size == 1 && url.segments.first().isBlank() -> null
            else -> url.segments.joinToString("/")
        }

    val arguments: Map<String,String>
        get() = url.parameters

    override fun toString() =
        "[$name]($url)"

    fun lookupCode(compilation: GroveDocCompilation): Path? =
        compilation.fileCollector.lookupCode(scheme, name, scope)

    fun lookupDef(compilation: GroveDocCompilation): Path? =
        compilation.fileCollector.lookupDef(name)

}