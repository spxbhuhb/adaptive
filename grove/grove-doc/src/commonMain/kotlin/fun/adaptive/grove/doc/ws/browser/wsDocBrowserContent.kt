package `fun`.adaptive.grove.doc.ws.browser

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.AvValue.Companion.withSpec

@Adaptive
fun wsDocBrowserContent(pane: WsPane<DocBrowserWsItem, *>): AdaptiveFragment {

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        pageHeader(pane)

        markdown(pane.data.item.withSpec<String>().spec)
     }

    return fragment()
}

@Adaptive
fun pageHeader(pane: WsPane<DocBrowserWsItem, *>) {
    column {
        paddingBottom { 32.dp }
        h2(pane.data.item.nameLike)
        itemPath(pane.data)
    }
}

@Adaptive
fun itemPath(item: DocBrowserWsItem) {
    val names = item.docPathNames()

    row {
        alignItems.center

        for (name in names.indices) {
            text(names[name]) .. textColors.onSurfaceVariant
            if (name < names.size - 1) {
                icon(Graphics.arrow_right) .. textColors.onSurfaceVariant
            }
        }
    }

}
