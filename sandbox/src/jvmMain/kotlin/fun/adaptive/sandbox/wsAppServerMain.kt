package `fun`.adaptive.sandbox

import `fun`.adaptive.app.basic.auth.AuthBasicInit.authBasicDefault
import `fun`.adaptive.app.basic.auth.authBasicJvm
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.iot.app.iotAppServerMain
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.ApplicationNodeType
import `fun`.adaptive.runtime.GlobalRuntimeContext
import kotlinx.coroutines.runBlocking

fun wsAppServerMain() {
    val logger = getLogger("application")
    logger.info("the application is starting")

    GlobalRuntimeContext.nodeType = ApplicationNodeType.Server

    runBlocking {
        iotCommon(loadStrings = false)
    }

    backend {

        settings {
            inline(
                "KTOR_PORT" to 8080,
                "KTOR_WIREFORMAT" to "json",
            )
        }
        inMemoryH2()

        authBasicJvm()
        ktor()

        iotAppServerMain()

    }.also {

        // at this point all backend components are created and mounted
        // so it is safe to use the database

        authBasicDefault("so")

        Runtime.getRuntime().addShutdownHook(Thread { it.stop() })

        while (it.isRunning) {
            Thread.sleep(1000)
        }
    }

    logger.info("the application stopped")
}