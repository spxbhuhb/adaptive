/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.UiClientApplicationData
import `fun`.adaptive.app.WsBrowserApplication
import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.app.ws.IotAppWsModule
import `fun`.adaptive.app.ws.inspect.BasicAppInspectWsModule
import `fun`.adaptive.chart.ChartWsModule
import `fun`.adaptive.document.DocWsModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.iot.IotWsModule
import `fun`.adaptive.iot.lib.zigbee.ZigbeeModule
import `fun`.adaptive.ui.LibUiModule
import `fun`.adaptive.ui.workspace.Workspace

fun main() {
    WsBrowserApplication(
        UiClientApplicationData(),
        // modules
        LibUiModule(),
        GroveRuntimeModule<Workspace>(),
        ChartWsModule(),
        DocWsModule(),
        IotWsModule(),
        ZigbeeModule<Workspace>(),
        BasicAppWsModule(),
        BasicAppInspectWsModule(),
        IotAppWsModule()
    ).main()
}