package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.auto.api.auto
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

    backend(wait = true) {

        settings {
            inline("KTOR_WIREFORMAT" to "JSON")
        }

        inMemoryH2("db")

        auth()
        ktor()

        auto()

        worker { ThermostatWorker() }
        service { ThermostatService() }

    }

}