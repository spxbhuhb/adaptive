package `fun`.adaptive.example.ws

import `fun`.adaptive.example.app.ExampleWsModule
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController

class ExampleSettingsViewBackend(
    module: ExampleWsModule<*>
) : WsSingularPaneController(module.workspace, module.EXAMPLE_SETTINGS_ITEM) {
    // This is where you can put functions like save, fetch etc.
}