package my.project.example.ws

import my.project.example.app.ExampleWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.Pane
import `fun`.adaptive.ui.mpw.model.PanePosition
import `fun`.adaptive.utility.UUID

fun MultiPaneWorkspace.wsExampleSettingsDef(
    module: ExampleWsModule<*>
) {
    addContentPaneBuilder(module.EXAMPLE_SETTINGS_KEY) {
        Pane(
            UUID(),
            workspace = this,
            Strings.settings,
            Graphics.settings,
            PanePosition.Center,
            module.EXAMPLE_SETTINGS_KEY,
            ExampleSettingsViewBackend(module)
        )
    }
}