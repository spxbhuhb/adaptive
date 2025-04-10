package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocList
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column

@Adaptive
fun docSequence(context : DocRenderContext, sequence: DocList): AdaptiveFragment {

    column {
        for (item in sequence.items) {
            docBlock(context, listOf(item))
        }
    }

    return fragment()
}