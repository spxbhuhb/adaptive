/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.auth.app.NoAuthClientModule
import `fun`.adaptive.sandbox.app.echo.app.EchoClientModule
import `fun`.adaptive.wireformat.api.Json

fun main() {
    basicBrowserClient {
        wireFormatProvider = Json
        module { NoAuthClientModule() }
        module { EchoClientModule() }
    }
}