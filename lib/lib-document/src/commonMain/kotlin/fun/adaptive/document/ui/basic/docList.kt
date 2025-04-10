package `fun`.adaptive.document.ui.basic

import `fun`.adaptive.document.model.DocList
import `fun`.adaptive.document.model.DocListItem
import `fun`.adaptive.document.ui.DocRenderContext
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun docList(context: DocRenderContext, list: DocList): AdaptiveFragment {

    column {
        maxWidth
        context.listContainer(list)

        for (item in list.items) {
            row {
                maxWidth .. fill.constrain
                label(context, item)
                docBlock(context, listOf(item.content))
            }
            if (item.subList != null) docList(context, item.subList)
        }
    }

    return fragment()
}

@Adaptive
private fun label(context: DocRenderContext, item: DocListItem) {
    val theme = context.theme

    val offset = if (item.bullet) 4.dp else 0.dp
    val indent = (if (item.bullet) theme.bulletListIndent else theme.numberListIndent) * (item.path.size - 1)
    val labelPadding = paddingLeft { indent + offset }

    row {
        labelPadding

        if (item.bullet) theme.listBulletContainer else theme.listNumberContainer

        if (item.bullet) {
            box { theme.listBullet }
        } else {
            text(theme.listPath(item)) .. theme.listNumber
        }
    }
}
