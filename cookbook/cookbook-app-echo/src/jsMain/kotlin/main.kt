/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.app.client.basic.BasicAppClientModule

fun main() {
    basicBrowserClient {
        module { BasicAppClientModule() }
    }
}