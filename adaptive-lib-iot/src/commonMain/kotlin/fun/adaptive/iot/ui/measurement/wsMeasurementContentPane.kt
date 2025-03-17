package `fun`.adaptive.iot.ui.measurement

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsMeasurementContentPane(pane : WsPane<MeasurementWsItem>): AdaptiveFragment {

    text(pane.model.name)

    return fragment()
}