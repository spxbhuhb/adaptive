package `fun`.adaptive.app.ws

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.chart.chartCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.document.docCommon
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.app.appHomeItem
import `fun`.adaptive.ui.app.appHomePane
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.snackbar.snackContainer
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.logic.WsUnitPaneController
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsFull
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun wsAppClientMain(
    transport: ServiceCallTransport,
    entryFun: (backend: BackendAdapter) -> Unit
) {
    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()
        chartCommon()
        docCommon()
        iotCommon()

        commonMainStringsStringStore0.load()

        entryFun(
            backend(transport) {
                auto()
                worker { SnackbarManager() }
            }
        )
    }
}

@Adaptive
fun wsAppAdaptiveMain() {

    val workspace = buildWorkspace(adapter())

    localContext(workspace) {
        wsFull(workspace)
    }

    snackContainer()
}

private fun buildWorkspace(adapter: AdaptiveAdapter): Workspace {

    val workspace = Workspace((adapter as AbstractAuiAdapter<*,*>).backend)

    with(workspace) {

        chartCommon()
        iotCommon()
        docCommon()
        appHomePane()

        workspace.addContent(appHomeItem)

        updateSplits()
    }

    return workspace
}

fun wsAppAdapterInit(adapter: AbstractAuiAdapter<*, *>, appFragmentFactory: AdaptiveFragmentFactory?) {

    with(adapter) {

        if (appFragmentFactory != null) {
            adapter.fragmentFactory += appFragmentFactory
        }

        uiCommon()
        groveRuntimeCommon()
        chartCommon()
        iotCommon()
        docCommon()

        with(defaultTextRenderData) {
            fontName = "Open Sans"
            fontSize = 16.sp
            fontWeight = 300
        }
    }

}