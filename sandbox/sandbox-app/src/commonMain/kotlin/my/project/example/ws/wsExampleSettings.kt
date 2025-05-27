package my.project.example.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.mpw.model.Pane

@Adaptive
fun wsExampleSettings(pane: Pane<ExampleSettingsViewBackend>): AdaptiveFragment {
    //devInfo { "displaying: ${pane.data} with backend ${pane.controller}" }
    return fragment()
}
