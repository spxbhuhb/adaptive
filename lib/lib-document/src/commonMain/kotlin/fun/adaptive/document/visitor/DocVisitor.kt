package `fun`.adaptive.document.visitor

import `fun`.adaptive.document.model.DocBlockFragment
import `fun`.adaptive.document.model.DocCodeFence
import `fun`.adaptive.document.model.DocDocument
import `fun`.adaptive.document.model.DocElement
import `fun`.adaptive.document.model.DocHeader
import `fun`.adaptive.document.model.DocBlockImage
import `fun`.adaptive.document.model.DocElementGroup
import `fun`.adaptive.document.model.DocInlineFragment
import `fun`.adaptive.document.model.DocInlineImage
import `fun`.adaptive.document.model.DocLink
import `fun`.adaptive.document.model.DocListItem
import `fun`.adaptive.document.model.DocParagraph
import `fun`.adaptive.document.model.DocQuote
import `fun`.adaptive.document.model.DocRule
import `fun`.adaptive.document.model.DocList
import `fun`.adaptive.document.model.DocText

abstract class DocVisitor<out R, in D> {

    abstract fun visitElement(element: DocElement, data: D): R

    open fun visitBlockFragment(docBlockFragment: DocBlockFragment, data: D): R =
        visitElement(docBlockFragment, data)

    open fun visitBlockImage(docBlockImage: DocBlockImage, data: D): R =
        visitElement(docBlockImage, data)

    open fun visitCodeFence(docCodeFence: DocCodeFence, data: D): R =
        visitElement(docCodeFence, data)

    open fun visitDocument(docDocument: DocDocument, data: D): R =
        visitElement(docDocument, data)

    open fun visitElementGroup(docElementGroup: DocElementGroup, data : D) : R =
        visitElement(docElementGroup, data)

    open fun visitHeader(docHeader: DocHeader, data: D): R =
        visitElement(docHeader, data)

    open fun visitInlineFragment(docInlineFragment: DocInlineFragment, data: D): R =
        visitElement(docInlineFragment, data)

    open fun visitInlineImage(docInlineImage: DocInlineImage, data: D): R =
        visitElement(docInlineImage, data)

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