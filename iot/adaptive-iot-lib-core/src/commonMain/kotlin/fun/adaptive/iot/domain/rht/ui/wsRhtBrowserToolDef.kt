package `fun`.adaptive.iot.domain.rht.ui

import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.domain.rht.AmvRelativeHumidityAndTemperature
import `fun`.adaptive.iot.generated.resources.dew_point
import `fun`.adaptive.iot.generated.resources.temperatureAndHumidity
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserConfig
import `fun`.adaptive.iot.space.ui.browser.SpaceBrowserToolController
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.builtin.collapseAll
import `fun`.adaptive.ui.builtin.expandAll
import `fun`.adaptive.ui.builtin.unfold_less
import `fun`.adaptive.ui.builtin.unfold_more
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPaneAction
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsRhtBrowserToolDef(
    module: IotWsModule<*>
) {

    val workspace = this

    val config = SpaceBrowserConfig(
        Strings.temperatureAndHumidity,
        Graphics.dew_point,
        AmvRelativeHumidityAndTemperature.WSIT_RHT_BROWSER_ITEM,
        headerKey = AmvRelativeHumidityAndTemperature.RHT_LIST_HEADER,
        itemKey = AmvRelativeHumidityAndTemperature.RHT_LIST_ITEM
    )

    val controller = SpaceBrowserToolController(workspace, config)

    config.controller = controller

    + WsPane(
        UUID(),
        workspace = workspace,
        Strings.temperatureAndHumidity,
        Graphics.dew_point,
        WsPanePosition.LeftTop,
        module.WSPANE_RHT_BROWSER_TOOL,
        actions = listOf(
            WsPaneAction(Graphics.unfold_more, Strings.expandAll, Unit) { controller.expandAll() },
            WsPaneAction(Graphics.unfold_less, Strings.collapseAll, Unit) { controller.collapseAll() },
        ),
        data = Unit,
        controller = controller
    )

    workspace.io {
        controller.start()
    }
}