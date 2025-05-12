package `fun`.adaptive.iot.point.ui

import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.common.AioTheme
import `fun`.adaptive.iot.common.status
import `fun`.adaptive.iot.common.timestamp
import `fun`.adaptive.iot.generated.resources.currentPointValue
import `fun`.adaptive.iot.generated.resources.newPointValue
import `fun`.adaptive.iot.generated.resources.pointValueSet
import `fun`.adaptive.iot.generated.resources.setPointValue
import `fun`.adaptive.iot.point.AioPointApi
import `fun`.adaptive.iot.point.AioPointSpec
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.iot.point.isSimulated
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.number.doubleOrNullInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textMedium
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.format
import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.item.AvStatus
import `fun`.adaptive.ui.value.AvUiValue
import kotlinx.datetime.Clock.System.now

@Adaptive
fun pointSummary(
    point: AvValue<AioPointSpec>?,
    theme: AioTheme = AioTheme.DEFAULT
) {
    val observed = valueFrom { InputContext() }
    val pointValue = valueFrom { AvUiValue<AvValue2>(adapter(), point?.markers[PointMarkers.CUR_VAL]) }
    val textualValue = extractValue(pointValue)

    grid {
        theme.pointSummary

        if (point == null) {
            box { maxSize .. backgrounds.lightOverlay }
        } else {
            text(point.name) .. noSelect
            text(textualValue) .. alignSelf.endCenter .. semiBoldFont
            timestamp(point.lastChange) .. noSelect .. textMedium .. alignSelf.endCenter
            status(point.status) .. alignSelf.endCenter
        }

        if (point != null && point.isSimulated) {
            contextPopup(observed) { hide ->
                theme.inlineEditorPopup .. width { 300.dp }
                setValuePopup(point, pointValue as? AvDouble, observed, hide)
            }
        }
    }

}

fun extractValue(pointValue: AvValue2?): String {
    when (pointValue) {
        is AvDouble -> return pointValue.value.format(1)
        is AvConvertedDouble -> return pointValue.convertedValue.format(1)
        else -> return "-"
    }
}


@Adaptive
fun setValuePopup(
    point: AvValue<AioPointSpec>,
    pointValue: AvDouble?,
    state: InputContext,
    hide: () -> Unit
) {
    var value: Double? = null
    val workspace = fragment().firstContext<Workspace>()

    val pointService = getService<AioPointApi>(adapter().transport)

    column {
        gap { 16.dp }

        column {
            h3(point.name)
            uuidLabel { point.uuid }
        }

        withLabel(Strings.currentPointValue, InputContext.DISABLED) {
            doubleOrNullInput(pointValue?.value, state = InputContext.DISABLED) { }
        }

        withLabel(Strings.newPointValue) {
            doubleOrNullInput(value, state = state) { v -> value = v; }
        }

        button(Strings.setPointValue) .. alignSelf.end .. onClick {
            workspace.io {
                val curVal = AvDouble(UUID.nil(), now(), AvStatus.OK, uuid4(), value ?: Double.NaN)
                pointService.setCurVal(curVal)
                successNotification(Strings.pointValueSet)
            }
            hide()
        }
    }
}
