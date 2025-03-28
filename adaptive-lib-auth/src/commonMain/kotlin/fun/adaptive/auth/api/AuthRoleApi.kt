/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.api

import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvValueId

@ServiceApi
interface AuthRoleApi {

    suspend fun all(): List<AuthRole>

    suspend fun add(name: String, spec: RoleSpec)

}