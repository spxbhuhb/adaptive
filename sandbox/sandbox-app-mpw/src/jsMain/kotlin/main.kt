/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.MpwBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.app.AppMainModuleMpw
import `fun`.adaptive.app.ui.mpw.admin.AppAdminModuleMpw
import `fun`.adaptive.app.ui.mpw.auth.AppAuthModuleMpw
import `fun`.adaptive.app.ui.mpw.inspect.AppInspectModuleMpw
import `fun`.adaptive.auth.app.AuthClientModule
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.grove.sheet.app.GroveSheetModuleClient
import `fun`.adaptive.grove.ufd.app.GroveUdfModuleMpw
import `fun`.adaptive.sandbox.app.mpw.app.AppProjectHomeModule
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.mpw.app.MultiPaneClientModule
import `fun`.adaptive.value.app.ValueClientModule

fun main() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { AuthClientModule() }
        module { ValueClientModule() }
        module { AppAdminModuleMpw() }
        module { AppInspectModuleMpw() }
        module { AppMainModuleMpw() }
        module { AppAuthModuleMpw() }
        module { AppProjectHomeModule() }

        module { MultiPaneClientModule() }
        module { GroveSheetModuleClient() }
        module { GroveUdfModuleMpw() }
    }
}