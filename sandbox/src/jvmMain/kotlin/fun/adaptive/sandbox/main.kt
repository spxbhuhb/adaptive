package `fun`.adaptive.sandbox

import `fun`.adaptive.app.JvmServerApplication.Companion.jvmServer
import `fun`.adaptive.app.server.BasicAppServerModule
import `fun`.adaptive.auth.app.AuthServerModule
import `fun`.adaptive.iot.IotServerModule
import `fun`.adaptive.lib.util.app.UtilServerModule

fun main() {
//    jvmServer {
//        module { UtilServerModule() }
//        module { AuthServerModule() }
//        module { BasicAppServerModule() }
//        module { IotServerModule() }
//    }
    wsAppServerMain()
}