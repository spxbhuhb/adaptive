package `fun`.adaptive.iot.device.ui.editor.dialogs

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot.device.ui.editor.DeviceEditorToolController
import `fun`.adaptive.iot.generated.resources.account_tree
import `fun`.adaptive.iot.generated.resources.addNetwork
import `fun`.adaptive.iot.generated.resources.pleaseSelectFromList
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.popup.modalCancelSave
import `fun`.adaptive.ui.popup.modalPopup
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textMedium

@Adaptive
fun addNetworkDialog(
    controller: DeviceEditorToolController,
    hide: () -> Unit
) {
    // FIXME split store mess
    val splitConfigStore = storeFor { SplitPaneViewBackend(SplitVisibility.Both, SplitMethod.FixFirst, 200.0, Orientation.Horizontal) }
    val splitConfig = valueFrom { splitConfigStore }

    modalPopup(Strings.addNetwork, hide, { modalCancelSave(hide) { } }) {
        size(800.dp, 600.dp)
        splitPane(
            splitConfig,
            { driverList(controller) },
            { verticalSplitDivider() },
            { driverSetup(controller) }
        ) .. borderTop(colors.lightOutline)
    }
}

@Adaptive
private fun driverList(
    controller: DeviceEditorToolController
) {

    val backend = selectInputBackend(
        controller.selectedDriverDef.value
    ) {
        options = controller.driversDefs()
        toText = { fragment().resolveString(it.driverNameKey) }
        onChange = { controller.selectedDriverDef.value = it }
        //toIcon = { fragment().resolveGraphics(it.driverIconKey) }
        toIcon = { Graphics.account_tree }
    }

    column {
        maxSize .. verticalScroll .. padding { 8.dp } .. backgrounds.surface
        selectInputList(backend, { selectInputOptionIconAndText(it) })
    }
}

@Adaptive
private fun driverSetup(
    controller: DeviceEditorToolController
) {
    val selectedDriverDef = valueFrom { controller.selectedDriverDef }

    if (selectedDriverDef == null) {
        text(Strings.pleaseSelectFromList) .. alignSelf.center .. textColors.onSurfaceMedium .. textMedium
    } else {
        column {
            maxSize .. verticalScroll .. padding { 16.dp }
            actualize(selectedDriverDef.newNetworkKey, emptyInstructions, controller)
        }
    }
}