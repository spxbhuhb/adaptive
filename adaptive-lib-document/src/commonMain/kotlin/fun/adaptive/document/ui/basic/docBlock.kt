package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocBlockElement
import `fun`.adaptive.document.model.DocCodeFence
import `fun`.adaptive.document.model.DocHeader
import `fun`.adaptive.document.model.DocList
import `fun`.adaptive.document.model.DocParagraph
import `fun`.adaptive.document.model.DocQuote
import `fun`.adaptive.document.model.DocRule
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column

@Adaptive
fun docBlock(context: DocRenderContext, children: List<DocBlockElement>) {
    column {
        for (element in children) {
            when (element) {
                is DocParagraph -> docParagraph(context, element)
                is DocList -> docList(context, element)
                is DocHeader -> docHeader(context, element)
                is DocCodeFence -> docCodeFence(context, element)
                is DocQuote -> docQuote(context, element)
                is DocRule -> docRule(context, element)
            }
        }
    }
}