/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.email

import `fun`.adaptive.email.service.EmailService
import `fun`.adaptive.email.store.EmailQueue
import `fun`.adaptive.email.store.EmailTable
import `fun`.adaptive.email.worker.EmailWorker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.server.builtin.service
import `fun`.adaptive.server.builtin.store
import `fun`.adaptive.server.builtin.worker

@Adaptive
fun email() {
    store { EmailTable }
    store { EmailQueue }

    service { EmailService() }

    worker { EmailWorker() }
}