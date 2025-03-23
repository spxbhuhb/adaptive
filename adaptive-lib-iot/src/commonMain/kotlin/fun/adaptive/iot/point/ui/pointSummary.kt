package `fun`.adaptive.iot.point.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.newPointValue
import `fun`.adaptive.adaptive_lib_iot.generated.resources.pointValueSet
import `fun`.adaptive.adaptive_lib_iot.generated.resources.setPointValue
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.isSimulated
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.number.doubleOrNullInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun pointSummary(
    point: AvItem<AioPointSpec>?,
    theme: AioTheme = AioTheme.DEFAULT
) {
    val observed = valueFrom { InputContext() }

    grid {
        theme.pointSummary

        if (point == null) {
            box { maxSize .. backgrounds.lightOverlay }
        } else {
            text(point.name) .. noSelect
            timestamp(point.timestamp) .. noSelect
            status(point.status) .. alignSelf.endCenter
        }

        if (point != null && point.isSimulated) {
            contextPopup(observed) { hide ->
                width { 300.dp } .. height { 152.dp } .. backgrounds.surfaceVariant .. borders.outline
                padding { 16.dp } .. cornerRadius { 4.dp } .. onClick { it.stopPropagation() }
                tabIndex { 0 } .. zIndex { 10000 }

                setValuePopup(point, observed, hide)
            }
        }
    }

}

@Adaptive
fun setValuePopup(point: AvItem<AioPointSpec>, state: InputContext, hide: () -> Unit) {
    var value : Double? = null
    val workspace = fragment().firstContext<Workspace>()

    column {
        gap { 16.dp }

        withLabel(Strings.newPointValue) {
            doubleOrNullInput(value, state = state) { v -> value = v; }
        }

        button(Strings.setPointValue) .. alignSelf.end .. onClick {
            workspace.io {
                getService<AioPointApi>(adapter().transport).setReadValue(point.uuid, value, AvStatus.OK)
                successNotification(Strings.pointValueSet)
            }
            hide()
        }
    }
}
