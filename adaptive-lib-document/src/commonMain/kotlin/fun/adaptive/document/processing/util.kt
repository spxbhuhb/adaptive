package `fun`.adaptive.document.processing

import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.resource.document.DocumentResourceSet

suspend fun DocumentResourceSet.fetchAndCompile() : DocDocument?{
    val uri = this.uri
    val content = this.readAll()

    when  {
        uri.endsWith(".md") -> MarkdownCompiler.compile(content.decodeToString())
        else -> null
    }.also {
        return it
    }
}