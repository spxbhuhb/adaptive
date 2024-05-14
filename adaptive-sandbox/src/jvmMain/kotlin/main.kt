/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.ktor.KtorWorker
import hu.simplexion.adaptive.sandbox.service.CounterService
import hu.simplexion.adaptive.sandbox.worker.CounterWorker
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.server.setting.dsl.propertyFile
import hu.simplexion.adaptive.server.setting.dsl.settings
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider

/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

fun main() {

    defaultWireFormatProvider = JsonWireFormatProvider()

    adaptive(AdaptiveServerAdapter<Any>(true)) {

        settings {
            propertyFile(optional = false) { "./adaptive-sandbox/etc/sandbox.properties" }
        }

        service { CounterService() }
        worker { CounterWorker() }

        worker { KtorWorker() }

    }

}