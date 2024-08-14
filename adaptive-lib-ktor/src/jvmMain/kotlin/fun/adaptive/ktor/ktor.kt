/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ktor

import `fun`.adaptive.auth.model.AccessDenied
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ktor.worker.KtorWorker
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adaptive
fun ktor() {
    WireFormatRegistry += AccessDenied
    worker { KtorWorker() }
}