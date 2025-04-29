package `fun`.adaptive.iot.domain.rht.ui.fragment

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.domain.rht.ui.controller.RhtBrowserContentController
import `fun`.adaptive.iot.domain.rht.ui.controller.RhtWsItem
import `fun`.adaptive.iot.generated.resources.filter
import `fun`.adaptive.iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.text.textInput2
import `fun`.adaptive.ui.input.text.textInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.value.AvUiList
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun rhtBrowserContent(pane: WsPane<RhtWsItem, RhtBrowserContentController>): AdaptiveFragment {

    val filter = valueFrom { pane.controller.rhtFilter }
    val subSpaces = valueFrom { AvUiList(adapter(), pane.data.uuid, SpaceMarkers.SUB_SPACES) }

    column {
        maxSize .. padding { 16.dp } .. backgrounds.surfaceVariant
        fillStrategy.constrain

        pageHeader(pane)
        rhtListHeader()

        column {
            gap { 12.dp } .. maxSize .. verticalScroll

            for (space in filter.filter(subSpaces)) {
                listItem(space)
            }
        }
    }

    return fragment()
}

@Adaptive
fun pageHeader(pane: WsPane<RhtWsItem, RhtBrowserContentController>) {

    val spaceFilter = pane.controller.rhtFilter
    val filter = valueFrom { pane.controller.rhtFilter }
    val search = textInputBackend(spaceFilter.value.search) { onChange = { spaceFilter.value = filter.copy(search = it ?: "") } }

    column {
        paddingBottom { 32.dp } .. gap { 24.dp }

        column {
            h2(Strings.temperatureAndHumidity)
            spacePath(pane.data)
        }

        grid {
            gap { 16.dp } .. colTemplate(1.fr, 300.dp) .. rowTemplate(38.dp)

            row {
                gap { 16.dp }
                quickFilter(filter.qf1) { spaceFilter.value = filter.copy(qf1 = filter.qf1.copy(selected = it)) }
                quickFilter(filter.qf2) { spaceFilter.value = filter.copy(qf2 = filter.qf2.copy(selected = it)) }
            }

            textInput2(search) .. inputPlaceholder { Strings.filter } .. maxWidth
        }
    }
}


@Adaptive
fun listItem(space: AvItem<*>) {
    row {
        height { 56.dp } .. maxWidth
        rhtListItem(space)
    }
}

@Adaptive
fun spacePath(item: RhtWsItem) {
    val names = item.spacePath

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
