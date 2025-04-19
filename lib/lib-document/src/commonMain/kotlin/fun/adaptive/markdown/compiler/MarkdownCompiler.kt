package `fun`.adaptive.markdown.compiler

import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.document.ui.DocumentTheme
import `fun`.adaptive.markdown.model.MarkdownElement
import `fun`.adaptive.markdown.transform.MarkdownAstDumpTransform.Companion.dump
import `fun`.adaptive.markdown.transform.MarkdownToDocTransform

object MarkdownCompiler {

    fun tokenize(text: String): List<MarkdownToken> =
        tokenizeInternal(text)

    fun parse(tokens : List<MarkdownToken>) : List<MarkdownElement> =
        parseInternal(tokens)

    fun compile(source : String, theme : DocumentTheme) : DocDocument =
        MarkdownToDocTransform(ast(source), theme).transform()

    fun ast(source : String) =
        parseInternal(tokenizeInternal(source))

    fun dump(source : String) : String =
        ast(source).dump()

}