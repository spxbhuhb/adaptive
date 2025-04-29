package `fun`.adaptive.iot.device.ui.editor.dialogs

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.general.Observable
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot.device.ui.editor.DeviceEditorToolController
import `fun`.adaptive.iot.generated.resources.addNetwork
import `fun`.adaptive.iot.generated.resources.pleaseSelectFromList
import `fun`.adaptive.resource.resolve.resolveString
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.input.select.item.selectInputOptionIconAndText
import `fun`.adaptive.ui.input.select.selectInputList
import `fun`.adaptive.ui.input.select.selectInputMappingBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.popup.modalCancelSave
import `fun`.adaptive.ui.popup.modalPopup
import `fun`.adaptive.ui.splitpane.verticalSplitDivider
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.utility.ComponentKey

@Adaptive
fun addNetworkDialog(
    controller: DeviceEditorToolController,
    hide: () -> Unit
) {
    // FIXME split store mess
    val splitConfigStore = storeFor { SplitPaneViewBackend(SplitVisibility.Both, SplitMethod.FixFirst, 200.0, Orientation.Horizontal) }
    val splitConfig = valueFrom { splitConfigStore }

    val selectedDriver = storeFor<ComponentKey?> { null }

    modalPopup(Strings.addNetwork, hide, { modalCancelSave(hide) { } }) {
        size(600.dp, 400.dp)
        splitPane(
            splitConfig,
            { driverList(controller, selectedDriver) },
            { verticalSplitDivider() },
            { driverSetup(selectedDriver) }
        )
    }
}

@Adaptive
private fun driverList(
    controller: DeviceEditorToolController,
    selectedDriver: Observable<ComponentKey?>
) {

    val selected = valueFrom { selectedDriver }

    val backend = selectInputMappingBackend<ComponentKey?, AioDriverDef>(
        selected,
        { it.driverKey },
    ) {
        options = controller.driversDefs()
        toText = { fragment().resolveString(it.driverNameKey) }
        // onChange = { selectedDriver.value = it }
        // toIcon = { fragment().resolveGraphics(it.driverIconKey) }
    }

    column {
        maxSize .. verticalScroll .. padding { 16.dp } .. backgrounds.surface
        selectInputList(backend, { selectInputOptionIconAndText(it) })
    }
}

@Adaptive
private fun driverSetup(selectedDriver: Observable<ComponentKey?>) {
    val driverKey = valueFrom { selectedDriver }

    if (driverKey == null) {
        text(Strings.pleaseSelectFromList)
    } else {
        actualize(driverKey)
    }
}