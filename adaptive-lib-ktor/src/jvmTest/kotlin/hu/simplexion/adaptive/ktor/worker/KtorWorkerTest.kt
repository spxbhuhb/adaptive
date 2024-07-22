/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor.worker

import hu.simplexion.adaptive.ktor.ktor
import hu.simplexion.adaptive.server.server
import org.junit.Test

class KtorWorkerTest {

    @Test
    fun ktorWorker() {

        server {
            ktor()
        }.also {
            it.stop()
        }
    }

}