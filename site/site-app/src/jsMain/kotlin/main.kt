/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.MpwBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.app.AppMainModuleMpw
import `fun`.adaptive.app.ui.mpw.inspect.AppInspectModuleMpw
import `fun`.adaptive.auth.app.NoAuthClientModule
import `fun`.adaptive.chart.app.ChartModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.grove.doc.app.GroveDocModuleMpw
import `fun`.adaptive.sandbox.app.CookbookModuleClient
import `fun`.adaptive.site.app.SiteModuleMpw
import `fun`.adaptive.site.appVersion
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.mpw.app.MultiPaneClientModule
import `fun`.adaptive.value.app.ValueClientModule


fun main() {
    wsBrowserClient(appVersion) {
        module { LibUiClientModule() }
        module { MultiPaneClientModule() }
        module { GroveRuntimeModule() }
        module { NoAuthClientModule() }
        module { ValueClientModule() }
        module { ChartModule() }
        module { CookbookModuleClient() }
        module { GroveDocModuleMpw() }
        module { SiteModuleMpw() }
        module { AppMainModuleMpw() }
        module { AppInspectModuleMpw() }
    }
}