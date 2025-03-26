package `fun`.adaptive.iot.app

import `fun`.adaptive.app.basic.auth.AuthBasicInit.authBasicDefault
import `fun`.adaptive.app.basic.auth.authBasicJvm
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.propertyFile
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.iot.lib.zigbee.ZigbeeModule
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.ApplicationNodeType
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.value.app.valueServerBackend
import `fun`.adaptive.value.persistence.FilePersistence
import `fun`.adaptive.value.valueCommon
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path

fun main() {
    val logger = getLogger("application")
    logger.info("the application is starting")

    GlobalRuntimeContext.nodeType = ApplicationNodeType.Server

    runBlocking {
        iotCommon(loadStrings = false)
        valueCommon()
    }

    ZigbeeModule<Unit>().apply { WireFormatRegistry.init() }

    backend {

        settings {
            propertyFile { "./etc/aio.properties" }
            inline("AIO_HISTORY_PATH" to "./var/history")
        }

        inMemoryH2()

        authBasicJvm()
        ktor()

        valueServerBackend(FilePersistence(Path("./var/values").ensure(), 2))
        iotServerBackend()

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