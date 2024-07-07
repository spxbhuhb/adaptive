/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.foundation.query.first
import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.server.builtin.ServerWorker
import hu.simplexion.adaptive.server.server
import hu.simplexion.adaptive.server.setting.dsl.inline
import hu.simplexion.adaptive.server.setting.dsl.settings
import org.junit.Test

class KtorWorkerTest {

    @Test
    fun ktorWorker() {

        server {

            settings {
                inline(
                    "KTOR_PORT" to 8080
                )
            }

            ktor()

        }.also {
            val worker = (it.first<ServerWorker>(true).workerImpl as KtorWorker)

            while (worker.applicationEngine == null) {
                worker.applicationEngine?.stop(0)
                Thread.sleep(20)
            }

        }
    }

}