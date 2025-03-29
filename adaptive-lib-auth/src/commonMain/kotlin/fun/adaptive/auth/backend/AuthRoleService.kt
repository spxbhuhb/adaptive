/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem

class AuthRoleService : AuthRoleApi, ServiceImpl<AuthRoleService> {

    val valueWorker by worker<AvValueWorker>()
    val authWorker by worker<AuthWorker>()
    val securityOfficer by lazy { authWorker.securityOfficer }

    override suspend fun all(): List<AuthRole> {
        ensureHas(securityOfficer)

        return valueWorker.queryByMarker(AuthMarkers.ROLE).map {
            it.asAvItem<RoleSpec>()
        }
    }

    override suspend fun add(name: String, spec: RoleSpec) {
        ensureHas(securityOfficer)

        //history(role)

        val roleValue = AvItem(
            name = name,
            type = AUTH_ROLE,
            parentId = null,
            markersOrNull = mutableMapOf(AuthMarkers.ROLE to null),
            friendlyId = name,
            spec = spec
        )

        valueWorker.execute {
            val uniqueName = valueWorker.queryByMarker(AuthMarkers.ROLE).none { it.name == name }
            require(uniqueName) { "role with the same name already exists" }

            this += roleValue
        }
    }

}
