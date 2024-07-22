/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth

import hu.simplexion.adaptive.auth.authCommon
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.lib.auth.service.PrincipalService
import hu.simplexion.adaptive.lib.auth.service.RoleService
import hu.simplexion.adaptive.lib.auth.service.SessionService
import hu.simplexion.adaptive.lib.auth.store.*
import hu.simplexion.adaptive.lib.auth.worker.SessionWorker
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.store
import hu.simplexion.adaptive.server.builtin.worker

@Adaptive
fun auth() {
    authCommon()

    store { principals }
    store { credentials }
    store { roles }
    store { roleContexts }
    store { roleGrants }
    store { roleMemberships }
    store { history }

    service { PrincipalService() }
    service { RoleService() }
    service { SessionService() }

    worker { SessionWorker() }
}