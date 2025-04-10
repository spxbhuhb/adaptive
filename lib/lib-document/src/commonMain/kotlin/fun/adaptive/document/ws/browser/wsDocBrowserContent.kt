package `fun`.adaptive.document.ws.browser

import `fun`.adaptive.document.generated.resources.documentation
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.AvValueId

@Adaptive
fun wsSpaceBrowserContent(pane: WsPane<DocBrowserWsItem, *>): AdaptiveFragment {

    val subSpaces = pane.subDocuments()

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        pageHeader(pane)
        listHeader(pane)

        for (space in subSpaces) {
            listItem(pane, space)
        }
    }

    return fragment()
}

@Adaptive
fun pageHeader(pane: WsPane<DocBrowserWsItem, *>) {
    column {
        paddingBottom { 32.dp }
        h2(Strings.documentation)
        spacePath(pane.data)
    }
}

@Adaptive
fun listHeader(pane: WsPane<DocBrowserWsItem, *>) {

}

@Adaptive
fun listItem(pane: WsPane<DocBrowserWsItem, *>, spaceId: AvValueId) {
    val space = pane.getDocument(spaceId) !!

    row {
        height { 32.dp } .. maxWidth
        actualize(pane.data.config.itemKey !!, emptyInstructions, space)
    }
}

@Adaptive
fun spacePath(item: DocBrowserWsItem) {
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
