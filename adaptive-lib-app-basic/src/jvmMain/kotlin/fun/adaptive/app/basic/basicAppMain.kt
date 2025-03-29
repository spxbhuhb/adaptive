package `fun`.adaptive.app.basic

import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.log.getLogger

fun basicAppMain() {

    val logger = getLogger("application")
    logger.info("the application is starting")

    backend {

        settings {
            inline(
                "KTOR_PORT" to 8080,
                "KTOR_WIREFORMAT" to "json",
            )
        }

        inMemoryH2()

        ktor()
    }.also {

        // at this point all backend components are created and mounted
        // so it is safe to use the database

        Runtime.getRuntime().addShutdownHook(Thread { it.stop() })

        while (it.isRunning) {
            Thread.sleep(1000)
        }
    }

    logger.info("the application stopped")
}