package `fun`.adaptive.iot.ui.space

import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.iot.model.space.AioSpace
import `fun`.adaptive.iot.model.space.SpaceBrowserWsItem
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsSpaceBrowserContent(pane: WsPane<SpaceBrowserWsItem>): AdaptiveFragment {

    val context = fragment().wsContext<AioWsContext>()
    val subSpaces = subSpaces(context, pane.model)

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface

        pageHeader(context, pane)
        listHeader(context, pane)

        for (space in subSpaces) {
            listItem(context, pane, space)
        }
    }

    return fragment()
}

@Adaptive
fun pageHeader(context: AioWsContext, pane: WsPane<SpaceBrowserWsItem>) {
    column {
        paddingBottom { 32.dp }
        h2(Strings.temperatureAndHumidity)
        spacePath(context, pane.model)
    }
}

@Adaptive
fun listHeader(context: AioWsContext, pane: WsPane<SpaceBrowserWsItem>) {

}

@Adaptive
fun listItem(context: AioWsContext, pane: WsPane<SpaceBrowserWsItem>, space: AioSpace) {
    row {
        gap { 16.dp }
        text(space.friendlyDisplayId)
        text(space.name)
    }
}

fun subSpaces(context: AioWsContext, item: SpaceBrowserWsItem): List<AioSpace> {
    val spaceId = item.spaceId
    val subSpaces = context.spaceMap.values.filter { it.parentId == spaceId }
    return subSpaces
}

@Adaptive
fun spacePath(context: AioWsContext, item: SpaceBrowserWsItem) {
    val names = spacePathNames(context, item)

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

fun spacePathNames(context: AioWsContext, item: SpaceBrowserWsItem): List<String> {
    val spaceId = item.spaceId
    val names = mutableListOf<String>()
    var space: AioSpace? = context.spaceMap[spaceId]
    while (space != null) {
        names.add(space.name)
        space = context.spaceMap[space.parentId]
    }
    return names.reversed()
}