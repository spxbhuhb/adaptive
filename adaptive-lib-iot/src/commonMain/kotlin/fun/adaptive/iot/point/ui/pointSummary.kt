package `fun`.adaptive.iot.point.ui

import `fun`.adaptive.adaptive_lib_iot.generated.resources.currentPointValue
import `fun`.adaptive.adaptive_lib_iot.generated.resources.newPointValue
import `fun`.adaptive.adaptive_lib_iot.generated.resources.pointValueSet
import `fun`.adaptive.adaptive_lib_iot.generated.resources.setPointValue
import `fun`.adaptive.document.ui.direct.h3
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
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.isSimulated
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.number.doubleOrNullInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.format
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.value.ui.AvUiValue
import kotlinx.datetime.Clock.System.now

@Adaptive
fun pointSummary(
    point: AvItem<AioPointSpec>?,
    theme: AioTheme = AioTheme.DEFAULT
) {
    val observed = valueFrom { InputContext() }
    val pointValue = valueFrom { AvUiValue<AvDouble>(adapter(), point?.markers[PointMarkers.CUR_VAL]) }

    grid {
        theme.pointSummary

        if (point == null) {
            box { maxSize .. backgrounds.lightOverlay }
        } else {
            text(point.name) .. noSelect
            text(pointValue?.value?.format(1) ?: "-") .. alignSelf.endCenter .. semiBoldFont
            timestamp(point.timestamp) .. noSelect .. textMedium .. alignSelf.endCenter
            status(point.status) .. alignSelf.endCenter
        }

        if (point != null && point.isSimulated) {
            contextPopup(observed) { hide ->
                width { 300.dp } .. backgrounds.surfaceVariant .. borders.outline
                padding { 16.dp } .. cornerRadius { 4.dp } .. onClick { it.stopPropagation() }
                tabIndex { 0 } .. zIndex { 10000 }

                setValuePopup(point, pointValue, observed, hide)
            }
        }
    }

}

@Adaptive
fun setValuePopup(
    point: AvItem<AioPointSpec>,
    pointValue: AvDouble?,
    state: InputContext,
    hide: () -> Unit
) {
    var value : Double? = null
    val workspace = fragment().firstContext<Workspace>()

    val pointService = getService<AioPointApi>(adapter().transport)

    column {
        gap { 16.dp }

        column {
            h3(point.name)
            uuidLabel { point.uuid }
        }

        withLabel(Strings.currentPointValue, InputContext.DISABLED) {
            doubleOrNullInput(pointValue?.value, state = InputContext.DISABLED) {  }
        }

        withLabel(Strings.newPointValue) {
            doubleOrNullInput(value, state = state) { v -> value = v; }
        }

        button(Strings.setPointValue) .. alignSelf.end .. onClick {
            workspace.io {
                val curVal = AvDouble(UUID.nil(), now(), AvStatus.OK, point.uuid, value ?: Double.NaN)
                pointService.setCurVal(curVal)
                successNotification(Strings.pointValueSet)
            }
            hide()
        }
    }
}
