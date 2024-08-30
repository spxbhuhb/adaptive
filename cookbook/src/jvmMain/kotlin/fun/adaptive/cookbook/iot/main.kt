package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.service.AutoService
import `fun`.adaptive.auto.worker.AutoWorker
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.auth

fun main() {

    iotCommon()
    autoCommon()

    backend(wait = true) {

        settings {
            inline("KTOR_WIREFORMAT" to "JSON")
        }

        inMemoryH2("db")

        auth()
        ktor()

        worker { AutoWorker() }
        service { AutoService() }

        worker { ThermostatWorker() }
        service { ThermostatService() }

    }

}