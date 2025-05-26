package my.project.example.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.log.devInfo
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsExampleSettings(pane: WsPane<ExampleSettingsViewBackend>): AdaptiveFragment {
    //devInfo { "displaying: ${pane.data} with backend ${pane.controller}" }
    return fragment()
}
