/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.WsBrowserApplication
import `fun`.adaptive.app.ws.WsAppModule
import `fun`.adaptive.app.ws.WsSandBoxModule
import `fun`.adaptive.chart.WsChartModule
import `fun`.adaptive.document.WsDocModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.iot.IotWsModule
import `fun`.adaptive.ui.LibUiModule
import `fun`.adaptive.ui.workspace.Workspace

fun main() {
    WsBrowserApplication(
        LibUiModule,
        GroveRuntimeModule<Workspace>(),
        WsChartModule(),
        WsDocModule(),
        IotWsModule(),
        WsAppModule,
        WsSandBoxModule
    ).main()
}