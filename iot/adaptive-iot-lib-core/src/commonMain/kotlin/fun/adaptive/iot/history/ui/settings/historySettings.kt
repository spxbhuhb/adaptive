package `fun`.adaptive.iot.history.ui.settings

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.iot.generated.resources.*
import `fun`.adaptive.iot.history.ui.HistoryContentController
import `fun`.adaptive.iot.history.ui.model.HistoryContentConfig
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.focusTableIconTheme
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.input.datetime.dateInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun historySettings(controller: HistoryContentController) {

    val config = copyOf { controller.config.value }
    var manual = false

    column {
        borderBottom(colors.lightOutline) .. paddingBottom { 16.dp }

        row {
            withLabel(Strings.start) { state ->
                dateInput(config.start, state) { v ->
                    config.update(config::start, v)
                    manual = true
                } .. width { 106.dp } .. alignItems.center
            } .. marginBottom { 3.dp } .. marginRight { 12.dp }

            withLabel(Strings.end) { state ->
                dateInput(config.end, state) { v ->
                    config.update(config::end, v)
                    manual = true
                } .. width { 106.dp } .. alignItems.center
            } .. marginBottom { 3.dp } .. marginRight { 8.dp }

            if (manual) {
                actionIcon(Graphics.play_circle, tooltip = Strings.applySettings, theme = focusTableIconTheme) .. onClick {
                    controller.config.value = config
                    manual = false
                } .. alignSelf.bottom
            } else {
                box {
                    actionIcon(Graphics.tune, tooltip = Strings.quickSetting, theme = tableIconTheme)
                    primaryPopup { hide ->
                        PopupTheme.default.inlineEditorPopup .. popupAlign.beforeBelow .. width { 300.dp }
                        quickInterval(config, hide)
                    }
                } .. alignSelf.bottom
            }
        }
    }
}

@Adaptive
fun quickInterval(config: HistoryContentConfig, hide: () -> Unit) {
    todo()
}