/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.auth

import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.lib.auth.service.PrincipalService
import `fun`.adaptive.lib.auth.service.RoleService
import `fun`.adaptive.lib.auth.service.SessionService
import `fun`.adaptive.lib.auth.store.*
import `fun`.adaptive.lib.auth.worker.SessionWorker
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.store
import `fun`.adaptive.backend.builtin.worker

@Adaptive
fun authJvm() {
    authCommon()

    store { principals }
    store { credentials }
    store { roles }
    store { roleGrants }
    store { roleMemberships }
    store { history }

    service { PrincipalService() }
    service { RoleService() }
    service { SessionService() }

    worker { SessionWorker() }
}