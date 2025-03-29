package `fun`.adaptive.sandbox

import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.iot.app.iotServerBackend
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.ApplicationNodeType
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.value.app.valueServerBackend
import `fun`.adaptive.value.persistence.FilePersistence
import `fun`.adaptive.value.valueCommon
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path

fun wsAppServerMain() {
    val logger = getLogger("application")
    logger.info("the application is starting")

    GlobalRuntimeContext.nodeType = ApplicationNodeType.Server

    runBlocking {
        iotCommon(loadStrings = false)
        valueCommon()
    }

    backend {

        settings {
            inline(
                "KTOR_PORT" to 8080,
                "KTOR_WIREFORMAT" to "json",
                "AIO_HISTORY_PATH" to "./var/history"
            )
        }
        inMemoryH2()

        ktor()

        valueServerBackend(FilePersistence(Path("./var/values").ensure(), 2))
        iotServerBackend()

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