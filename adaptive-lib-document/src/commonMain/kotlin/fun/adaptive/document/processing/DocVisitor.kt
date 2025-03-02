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

abstract class DocVisitor<out R, in D> {

    abstract fun visitElement(element: DocElement, data: D): R

    open fun visitCodeFence(docCodeFence: DocCodeFence, data: D): R =
        visitElement(docCodeFence, data)

    open fun visitDocument(docDocument: DocDocument, data: D): R =
        visitElement(docDocument, data)

    open fun visitHeader(docHeader: DocHeader, data: D): R =
        visitElement(docHeader, data)

    open fun visitLink(docLink: DocLink, data: D): R =
        visitElement(docLink, data)

    open fun visitList(docList: DocList, data: D): R =
        visitElement(docList, data)

    open fun visitListItem(docListItem: DocListItem, data: D): R =
        visitElement(docListItem, data)

    open fun visitParagraph(docParagraph: DocParagraph, data: D): R =
        visitElement(docParagraph, data)

    open fun visitQuote(docQuote: DocQuote, data: D): R =
        visitElement(docQuote, data)

    open fun visitRule(docRule: DocRule, data: D): R =
        visitElement(docRule, data)

    open fun visitText(docText: DocText, data: D): R =
        visitElement(docText, data)

}