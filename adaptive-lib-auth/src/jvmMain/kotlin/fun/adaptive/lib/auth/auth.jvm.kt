/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.auth

import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.auth.backend.AuthPrincipalService
import `fun`.adaptive.auth.backend.AuthRoleService
import `fun`.adaptive.auth.backend.AuthSessionService
import `fun`.adaptive.auth.backend.AuthSessionWorker
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.worker

@Adaptive
fun authJvm() {
    authCommon()

    service { AuthPrincipalService() }
    service { AuthRoleService() }
    service { AuthSessionService() }

    worker { AuthSessionWorker() }
}