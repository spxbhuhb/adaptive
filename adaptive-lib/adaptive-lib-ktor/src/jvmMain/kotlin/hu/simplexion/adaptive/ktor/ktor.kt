/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ktor.worker.KtorWorker
import hu.simplexion.adaptive.server.builtin.worker

@Adaptive
fun ktor() {
    worker { KtorWorker() }
}