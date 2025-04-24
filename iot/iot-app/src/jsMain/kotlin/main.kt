/*
 * Copyright © 2020-2024} Simplexion} Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.WsBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.IotAppWsModule
import `fun`.adaptive.app.ws.admin.AppAdminWsModule
import `fun`.adaptive.app.ws.auth.AppAuthWsModule
import `fun`.adaptive.app.ws.inspect.AppInspectWsModule
import `fun`.adaptive.auth.app.AuthClientModule
import `fun`.adaptive.chart.app.ChartModule
import `fun`.adaptive.document.app.DocWsModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.iot.lib.zigbee.app.ZigbeeDriverWsModule
import `fun`.adaptive.iot.node.app.NodeDriverWsModule
import `fun`.adaptive.iot.sim.app.SimDriverWsModule
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.value.app.ValueClientModule

fun main() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { AuthClientModule() }
        module { ValueClientModule() }
        module { ChartModule() }
        //module { DocWsModule() }
        module { IotWsModule() }
        module { SimDriverWsModule() }
        module { NodeDriverWsModule() }
        module { ZigbeeDriverWsModule() }
        module { AppAdminWsModule() }
        module { AppInspectWsModule() }
        module { AppMainWsModule() }
        module { AppAuthWsModule() }
        module { IotAppWsModule() }
    }
}