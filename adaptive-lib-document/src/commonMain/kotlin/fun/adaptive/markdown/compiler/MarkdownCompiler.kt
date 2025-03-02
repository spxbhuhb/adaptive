package `fun`.adaptive.markdown.compiler

import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.markdown.model.MarkdownElement
import `fun`.adaptive.markdown.transform.MarkdownAstDumpTransform.Companion.dump
import `fun`.adaptive.markdown.transform.MarkdownToDocTransform

object MarkdownCompiler {

    fun tokenize(text: String): List<MarkdownToken> =
        tokenizeInternal(text)

    fun parse(tokens : List<MarkdownToken>) : List<MarkdownElement> =
        parseInternal(tokens)

    fun compile(source : String) : DocDocument =
        MarkdownToDocTransform(ast(source)).transform()

    fun ast(source : String) =
        parseInternal(tokenizeInternal(source))

    fun dump(source : String) : String =
        ast(source).dump()

}