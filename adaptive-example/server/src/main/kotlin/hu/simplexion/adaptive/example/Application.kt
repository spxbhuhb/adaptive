package hu.simplexion.adaptive.example

import hu.simplexion.adaptive.example.service.CounterService
import hu.simplexion.adaptive.example.worker.CounterWorker
import hu.simplexion.adaptive.ktor.KtorWorker
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.server.setting.dsl.inline
import hu.simplexion.adaptive.server.setting.dsl.settings
import hu.simplexion.adaptive.wireformat.withJson

fun main() {

    withJson()

    server(true) {

        settings {
            inline(
                "KTOR_PORT" to 8080,
                "KTOR_STATIC" to "../browserApp/build/processedResources/js/main",
                "COUNTER_LIMIT" to 2000,
                "COUNTER_IDLE_INTERVAL" to 60
            )
        }

        service { CounterService() }
        worker { CounterWorker() }

        worker { KtorWorker() }

    }

}
