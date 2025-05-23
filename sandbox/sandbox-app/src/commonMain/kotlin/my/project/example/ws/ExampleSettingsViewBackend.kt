package my.project.example.ws

import my.project.example.api.ExampleApi
import my.project.example.app.ExampleWsModule
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController

class ExampleSettingsViewBackend(
    module: ExampleWsModule<*>
) : WsSingularPaneController(module.workspace, module.EXAMPLE_SETTINGS_ITEM) {
    // This is where you can put functions like save, fetch etc.

    val exampleService = getService<ExampleApi>(backend.transport)

}