package `fun`.adaptive.example.ws

import `fun`.adaptive.example.app.ExampleWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsExampleSettingsDef(
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