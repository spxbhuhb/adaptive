/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.WsBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.inspect.AppInspectWsModule
import `fun`.adaptive.auth.app.NoAuthClientModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.grove.doc.app.GroveDocWsModule
import `fun`.adaptive.sandbox.app.CookbookClientModule
import `fun`.adaptive.site.SiteWsModule
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.mpw.app.MultiPaneClientModule
import `fun`.adaptive.value.app.ValueClientModule


fun main() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { MultiPaneClientModule() }
        module { GroveRuntimeModule() }
        module { NoAuthClientModule() }
        module { ValueClientModule() }
        //module { ChartWsModule() }
        module { CookbookClientModule() }
        module { GroveDocWsModule() }
        module { SiteWsModule() }
        module { AppMainWsModule() }
        module { AppInspectWsModule() }
    }
}