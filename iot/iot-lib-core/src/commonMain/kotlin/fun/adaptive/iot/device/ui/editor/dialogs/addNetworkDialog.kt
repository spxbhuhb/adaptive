package `fun`.adaptive.iot.device.ui.editor.dialogs

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.iot.device.ui.editor.DeviceEditorToolController
import `fun`.adaptive.iot.generated.resources.addNetwork
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.popup.modalCancelSave
import `fun`.adaptive.ui.popup.modalPopup
import `fun`.adaptive.ui.splitpane.splitPaneDivider

@Adaptive
fun addNetworkDialog(
    controller: DeviceEditorToolController,
    hide: () -> Unit
) {
    // FIXME split store mess
    val splitConfigStore = storeFor { SplitPaneConfiguration(SplitVisibility.Both, SplitMethod.FixFirst, 200.0, Orientation.Horizontal) }
    val splitConfig = valueFrom { splitConfigStore }


    modalPopup(Strings.addNetwork, hide, { modalCancelSave(hide) { } }) {
        size(600.dp, 400.dp)
        splitPane(
            splitConfig,
            { driverList(controller) },
            { splitPaneDivider() },
            { driverSetup() }
        )
    }
}

@Adaptive
private fun driverList(controller: DeviceEditorToolController) {
    column {
        maxSize .. verticalScroll
        for (def in controller.driversDefs()) {
            text(def.driverKey)
        }
    }
}

@Adaptive
private fun driverSetup() {

}