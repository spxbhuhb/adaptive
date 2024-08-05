/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ktor

import hu.simplexion.adaptive.auth.model.AccessDenied
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ktor.worker.KtorWorker
import hu.simplexion.adaptive.server.builtin.worker
import hu.simplexion.adaptive.wireformat.WireFormatRegistry

@Adaptive
fun ktor() {
    WireFormatRegistry += AccessDenied
    worker { KtorWorker() }
}