package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.adaptive_lib_iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.space.SpaceMarkers
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.builtin.arrow_right
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.ui.AvUiList

@Adaptive
fun wsSpaceBrowserContent(pane: WsPane<SpaceBrowserWsItem, *>): AdaptiveFragment {

    val subSpaces = valueFrom { AvUiList(adapter(), pane.data.uuid, SpaceMarkers.SUB_SPACES) }

    column {
        maxSize .. padding { 16.dp } .. backgrounds.surfaceVariant
        fill.constrain

        pageHeader(pane)
        listHeader(pane)

        column {
            gap { 12.dp } .. maxSize .. verticalScroll

            for (space in subSpaces) {
                listItem(pane, space)
            }
        }
    }

    return fragment()
}

@Adaptive
fun pageHeader(pane: WsPane<SpaceBrowserWsItem, *>) {

    var status1 = "Összes"
    var status2 = "Összes"
    var search = ""

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
                quickFilter(status1, listOf("Összes", "Aktív", "Nem elérhető"), { this }) { v -> status1 = v }
                quickFilter(status2, listOf("Összes", "Rendben", "Riasztás"), { this }) { v -> status2 = v }
            }

            textInput(search) { v -> search = v } .. inputPlaceholder { "Szűrés" } .. maxWidth
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
