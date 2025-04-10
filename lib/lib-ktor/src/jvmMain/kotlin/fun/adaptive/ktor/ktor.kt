/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor

import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ktor.worker.KtorWorker
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.wireformat.WireFormatRegistry
import io.ktor.server.application.Application

@Adaptive
fun ktor(port : Int = 8080) {
    WireFormatRegistry += AccessDenied
    worker { KtorWorker(port) }
}