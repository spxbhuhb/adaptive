/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor.worker

import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.backend.backend
import org.junit.Test

class KtorWorkerTest {

    @Test
    fun ktorWorker() {

        backend {
            ktor()
        }.also {
            it.stop()
        }
    }

}