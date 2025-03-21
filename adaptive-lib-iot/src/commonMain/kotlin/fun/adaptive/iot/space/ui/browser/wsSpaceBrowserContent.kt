package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsSpaceBrowserContent(pane: WsPane<SpaceBrowserWsItem, *>): AdaptiveFragment {

    val subSpaces = pane.subSpaces()

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
fun pageHeader(pane: WsPane<SpaceBrowserWsItem, *>) {
    column {
        paddingBottom { 32.dp }
        h2(Strings.temperatureAndHumidity)
        spacePath(pane.data)
    }
}

@Adaptive
fun listHeader(pane: WsPane<SpaceBrowserWsItem, *>) {

}

@Adaptive
fun listItem(pane: WsPane<SpaceBrowserWsItem, *>, spaceId: AioValueId) {
    val space = pane.getSpace(spaceId) !!

    row {
        height { 32.dp } .. maxWidth
        actualize(pane.data.config.itemKey !!, emptyInstructions, space)
    }
}

@Adaptive
fun spacePath(item: SpaceBrowserWsItem) {
    val names = item.spacePathNames()

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
