package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.*
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column

@Adaptive
fun docBlock(context: DocRenderContext, children: List<DocBlockElement>) {
    column {
        context.theme.blockGap

        for (element in children) {
            when (element) {
                is DocParagraph -> docParagraph(context, element)
                is DocList -> docList(context, element)
                is DocHeader -> docHeader(context, element)
                is DocCodeFence -> docCodeFence(context, element)
                is DocQuote -> docQuote(context, element)
                is DocRule -> docRule(context, element)
                is DocBlockImage -> docBlockImage(context, element)
                is DocElementGroup -> {
                    for (child in element.content) {
                        docBlock(context, listOf(child))
                    }
                }
                is DocBlockFragment -> docBlockFragment(context, element)
            }
        }
    }
}