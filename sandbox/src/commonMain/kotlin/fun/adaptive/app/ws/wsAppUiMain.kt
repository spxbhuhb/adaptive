package `fun`.adaptive.app.ws

import `fun`.adaptive.chart.chartCommon
import `fun`.adaptive.cookbook.eco
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.snackbar.snackContainer
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.ui.workspace.wsFull
import `fun`.adaptive.utility.UUID

@Adaptive
fun wsAppUiMain() {

    val workspace = buildWorkspace()

    localContext(workspace) {
        wsFull(workspace)
    }

    snackContainer()
}

private fun buildWorkspace(): Workspace {

    val workspace = Workspace()

    with(workspace) {

        chartCommon()
        iotCommon()

        addContentPaneBuilder("home") {
            WsPane(
                UUID(),
                "Home",
                Graphics.eco,
                WsPanePosition.Center,
                "home",
                Unit
            )
        }

        updateSplits()
    }

    return workspace
}

fun wsAppUiInit(adapter: AbstractAuiAdapter<*, *>, appFragmentFactory: AdaptiveFragmentFactory?) {

    with(adapter) {

        if (appFragmentFactory != null) {
            adapter.fragmentFactory += appFragmentFactory
        }

        uiCommon()
        groveRuntimeCommon()
        chartCommon()
        iotCommon()

        with(defaultTextRenderData) {
            fontName = "Open Sans"
            fontSize = 16.sp
            fontWeight = 300
        }
    }

}