/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.context.ensureOneOf
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem

class AuthRoleService : AuthRoleApi, ServiceImpl<AuthRoleService> {

    companion object {
        var addRoles = emptyArray<AuthRoleId>()
        var getRoles = emptyArray<AuthRoleId>()

        lateinit var worker: AvValueWorker
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        worker = safeAdapter.firstImpl<AvValueWorker>()
    }

    override suspend fun all(): List<AuthRole> =
        ensureOneOf(*getRoles).let {
            worker.queryByMarker(AuthMarkers.ROLE).map {
                it.asAvItem<RoleSpec>()
            }
        }

    override suspend fun add(name: String, spec: RoleSpec) {
        ensureOneOf(*addRoles)

        //history(role)

        val roleValue = AvItem(
            name = name,
            type = AUTH_ROLE,
            parentId = null,
            markersOrNull = null,
            friendlyId = name,
            specific = spec
        )

        worker.execute {
            val uniqueName = worker.queryByMarker(AuthMarkers.ROLE).none { it.name == name }
            require(uniqueName) { "role with the same name already exists" }

            this += roleValue
        }
    }

}
