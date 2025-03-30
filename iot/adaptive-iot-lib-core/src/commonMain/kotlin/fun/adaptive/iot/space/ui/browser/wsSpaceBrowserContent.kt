package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.filter.QuickFilterModel
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.value.AvUiList
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.item.AvItem

@Adaptive
fun wsSpaceBrowserContent(pane: WsPane<SpaceBrowserWsItem, SpaceBrowserContentController>): AdaptiveFragment {

    val filter = valueFrom { spaceFilter }
    val subSpaces = valueFrom { AvUiList(adapter(), pane.data.uuid, SpaceMarkers.SUB_SPACES) }

    column {
        maxSize .. padding { 16.dp } .. backgrounds.surfaceVariant
        fill.constrain

        pageHeader(pane)
        listHeader(pane)

        column {
            gap { 12.dp } .. maxSize .. verticalScroll

            for (space in filter.filter(subSpaces)) {
                listItem(pane, space)
            }
        }
    }

    return fragment()
}

val spaceFilter = storeFor { SpaceFilter() }

data class SpaceFilter(
    val search : String = "",
    val qf1 : QuickFilterModel<String> = QuickFilterModel(Strings.all, listOf(Strings.all, Strings.active, Strings.down), { it }),
    val qf2 : QuickFilterModel<String> = QuickFilterModel(Strings.all, listOf(Strings.all, Strings.ok, Strings.alarm), { it })
) {

    fun filter(items : List<AvItem<*>>) : List<AvItem<*>> =
        items.filter { matches(it) }

    fun matches(item : AvItem<*>) : Boolean {

        when (qf1.selected) {
            Strings.active -> if (!item.status.isActive) return false
            Strings.down -> if (!item.status.isDown) return false
        }

        when (qf2.selected) {
            Strings.ok -> if (!item.status.isOk) return false
            Strings.alarm -> if (!item.status.isAlarm) return false
        }

        if (search.isEmpty()) return true
        val lcSearch = search.lowercase()

        if (lcSearch in item.name.lowercase()) return true
        if (lcSearch in item.friendlyId.lowercase()) return true

        return false
    }

}


@Adaptive
fun pageHeader(pane: WsPane<SpaceBrowserWsItem, *>) {

    val filter = valueFrom { spaceFilter }

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

            textInput(filter.search) { v -> spaceFilter.value = filter.copy(search = v) } ..
                inputPlaceholder { Strings.filter } .. maxWidth
        }
    }
}

@Adaptive
fun listHeader(pane: WsPane<SpaceBrowserWsItem, *>) {
    actualize(pane.data.config.headerKey, emptyInstructions)
}

@Adaptive
fun listItem(pane: WsPane<SpaceBrowserWsItem, *>, space: AvItem<*>) {
    row {
        height { 56.dp } .. maxWidth
        actualize(pane.data.config.itemKey, emptyInstructions, space)
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
