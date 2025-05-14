package my.project.example.ws

import my.project.example.app.ExampleWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsExampleSettingsDef(
    module: ExampleWsModule<*>
) {
    addContentPaneBuilder(module.EXAMPLE_SETTINGS_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.settings,
            Graphics.settings,
            WsPanePosition.Center,
            module.EXAMPLE_SETTINGS_KEY,
            module.EXAMPLE_SETTINGS_ITEM,
            ExampleSettingsViewBackend(module)
        )
    }
}