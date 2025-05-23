package `fun`.adaptive.value.example

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.value.app.ValueClientModule

fun main() {
    basicBrowserClient {
        module { ValueClientModule() }
    }
}