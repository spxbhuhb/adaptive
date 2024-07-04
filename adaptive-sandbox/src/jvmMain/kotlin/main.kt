/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.ktor.worker.KtorWorker
import hu.simplexion.adaptive.sandbox.service.CounterService
import hu.simplexion.adaptive.sandbox.worker.CounterWorker
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.server.setting.dsl.propertyFile
import hu.simplexion.adaptive.server.setting.dsl.settings
import hu.simplexion.adaptive.wireformat.withJson

fun main() {

    withJson()

    server(wait = true) {

        settings {
            propertyFile(optional = false) { "./etc/sandbox.properties" }
        }

        service { CounterService() }
        worker { CounterWorker() }

        worker { KtorWorker() }

    }

}