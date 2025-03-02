package `fun`.adaptive.document.processing

import `fun`.adaptive.document.model.DocCodeFence
import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.document.model.DocElement
import `fun`.adaptive.document.model.DocHeader
import `fun`.adaptive.document.model.DocLink
import `fun`.adaptive.document.model.DocListItem
import `fun`.adaptive.document.model.DocParagraph
import `fun`.adaptive.document.model.DocQuote
import `fun`.adaptive.document.model.DocRule
import `fun`.adaptive.document.model.DocList
import `fun`.adaptive.document.model.DocText
import `fun`.adaptive.document.processing.DocDumpVisitor.DocVisitorData

class DocDumpVisitor : DocVisitor<Unit, DocVisitorData>() {

    class DocVisitorData(
        val indent : String = "  ",
        var level : Int = 0,
        var output : StringBuilder = StringBuilder()
    ) {
        operator fun plusAssign(s: String) {
            output.append(indent.repeat(level))
            output.appendLine(s)
        }

        fun withIndent(block: DocVisitorData.() -> String) {
            output.append(indent.repeat(level + 1))
            output.appendLine(block())
        }
    }

    override fun visitElement(element: DocElement, data: DocVisitorData) {
        data.level ++
        element.acceptChildren(this, data)
        data.level --
    }

    override fun visitCodeFence(docCodeFence: DocCodeFence, data: DocVisitorData) {
        data += "CODE FENCE  language=${docCodeFence.language}  style=${docCodeFence.style} "
        data.withIndent { "content: ${docCodeFence.code}" }
        super.visitCodeFence(docCodeFence, data)
    }

    override fun visitDocument(docDocument: DocDocument, data: DocVisitorData) {
        data += "DOCUMENT"
        super.visitDocument(docDocument, data)
    }

    override fun visitHeader(docHeader: DocHeader, data: DocVisitorData) {
        data += "HEADER  level=${docHeader.level}  style=${docHeader.style} "
        super.visitHeader(docHeader, data)
    }

    override fun visitLink(docLink: DocLink, data: DocVisitorData) {
        data += "LINK  url=${docLink.url}  style=${docLink.style}"
        data.withIndent { "text: ${docLink.text}" }
        super.visitLink(docLink, data)
    }

    override fun visitList(docList: DocList, data: DocVisitorData) {
        data += "LIST  standalone=${docList.standalone}  style=${docList.style}"
        super.visitList(docList, data)
    }

    override fun visitListItem(docListItem: DocListItem, data: DocVisitorData) {
        data += "LIST ITEM  path=${docListItem.path}  bullet=${docListItem.bullet}  style=${docListItem.style}"
        super.visitListItem(docListItem, data)
    }

    override fun visitParagraph(docParagraph: DocParagraph, data: DocVisitorData) {
        data += "PARAGRAPH  standalone=${docParagraph.standalone}  style=${docParagraph.style}"
        super.visitParagraph(docParagraph, data)
    }

    override fun visitQuote(docQuote: DocQuote, data: DocVisitorData) {
        data += "QUOTE  style=${docQuote.style}"
        super.visitQuote(docQuote, data)
    }

    override fun visitRule(docRule: DocRule, data: DocVisitorData) {
        data += "RULE  style=${docRule.style}"
        super.visitRule(docRule, data)
    }

    override fun visitText(docText: DocText, data: DocVisitorData) {
        data += "TEXT  style=${docText.style}"
        data.withIndent { "text: ${docText.text}" }
        super.visitText(docText, data)
    }

    companion object {

        fun DocElement.dump(): String =
            DocVisitorData().also { this.accept(DocDumpVisitor(), it) }.output.toString()

    }
}