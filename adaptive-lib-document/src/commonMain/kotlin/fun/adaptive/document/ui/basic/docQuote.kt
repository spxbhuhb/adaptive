package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocQuote
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.wrap.wrapFromLeft

@Adaptive
fun docQuote(context: DocRenderContext, quote: DocQuote): AdaptiveFragment {

    val wrapperSize = context.theme.quoteDecorationWidth

    wrapFromLeft(
        wrapperSize,
        wrapper = { box { maxHeight .. width { wrapperSize } .. backgrounds.friendly .. cornerRadius { 2.dp } } },
    ) {
        box {
            backgrounds.surfaceVariant .. cornerRadius { 2.dp } .. padding { 8.dp }
            docBlock(context, quote.content)
        }
    }

    return fragment()
}