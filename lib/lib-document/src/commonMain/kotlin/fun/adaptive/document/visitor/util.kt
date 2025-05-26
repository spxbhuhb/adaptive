package `fun`.adaptive.document.visitor

import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.resource.document.DocumentResourceSet

suspend fun DocumentResourceSet.fetchAndCompile(
    theme : DocumentTheme
): DocDocument? {
    try {
        val uri = this.uri
        val content = this.readAll()

        when {
            uri.endsWith(".md") -> MarkdownCompiler.compile(content.decodeToString(), theme)
            else -> null
        }.also {
            return it
        }
    } catch (e: Exception) {
        getLogger("document").warning("couldn't compile document: $uri", e)
        return null
    }
}