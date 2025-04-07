/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.WsBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.inspect.AppInspectWsModule
import `fun`.adaptive.auth.app.NoAuthClientModule
import `fun`.adaptive.cookbook.app.CookbookWsModule
import `fun`.adaptive.document.app.DocWsModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.site.SiteWsModule
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.value.app.ValueClientModule


fun main() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { NoAuthClientModule() }
        module { ValueClientModule() }
        //module { ChartWsModule() }
        module { CookbookWsModule() }
        module { DocWsModule() }
        module { SiteWsModule() }
        module { AppMainWsModule() }
        module { AppInspectWsModule() }
    }
}