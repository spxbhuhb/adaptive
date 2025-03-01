package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocList
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun docList(context : DocRenderContext, list: DocList): AdaptiveFragment {

    column {
        for (item in list.children.withIndex()) {
            row {
                label(context, list, item.index)
                docBlock(context, listOf(item.value))
            }
        }
    }

    return fragment()
}

@Adaptive
private fun label(context : DocRenderContext, list : DocList, index : Int) {
    box {
        paddingTop { 7.dp } .. paddingLeft { 16.dp * (list.level - 1) } .. width { 16.dp * list.level - 4.dp }

        if (list.bullet) {
            box {
                context.theme.listBullet
            }
        } else {
            text("$index")
        }
    }
}
