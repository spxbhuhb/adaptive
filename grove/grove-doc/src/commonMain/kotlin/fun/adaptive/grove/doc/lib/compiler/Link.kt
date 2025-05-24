package `fun`.adaptive.grove.doc.lib.compiler

import kotlinx.io.files.Path

/**
 * A link in Markdown: `[name](scheme://scope?arguments)
 *
 * @property    original    `scheme://scope?arguments`.
 */
data class Link(
    val name: String,
    val original: String,
    val scheme: String,
    val scope: String? = null,
    val arguments: String? = null
) {
    override fun toString() =
        "[$name]($scheme://${scope ?: ""}${arguments?.let { "($it)" } ?: ""})"

    fun lookupCode(compilation: GroveDocCompilation): Path? =
        compilation.fileCollector.lookupCode(scheme, name, scope)
}