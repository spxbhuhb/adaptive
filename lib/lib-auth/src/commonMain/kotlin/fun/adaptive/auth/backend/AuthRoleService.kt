/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.context.ensureHas
import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.asAvItem
import `fun`.adaptive.value.util.serviceSubscribe

class AuthRoleService : AuthRoleApi, ServiceImpl<AuthRoleService> {

    val valueWorker by worker<AvValueWorker>()
    val authWorker by worker<AuthWorker>()
    val securityOfficer by lazy { authWorker.securityOfficer }

    override suspend fun all(): List<AuthRole> {
        ensureLoggedIn()

        return valueWorker.queryByMarker(AuthMarkers.ROLE).map {
            it.asAvItem<RoleSpec>()
        }
    }

    override suspend fun save(roleId: AvValueId?, name: String, spec: RoleSpec) {
        ensureHas(securityOfficer)

        //history(role)

        if (roleId == null) {
            add(name, spec)
        } else {
            update(roleId, name, spec)
        }
    }

    private suspend fun add(name: String, spec: RoleSpec) {

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

    private suspend fun update(roleId: AvValueId, name: String, spec: RoleSpec) {
        valueWorker.update<AvItem<*>>(roleId) { item ->
            item.asAvItem<RoleSpec>().copy(name = name, spec = spec)
        }
    }

    override suspend fun subscribe(subscriptionId: AvValueSubscriptionId): List<AvSubscribeCondition> {
        ensureHas(securityOfficer)
        return serviceSubscribe(valueWorker, subscriptionId, AuthMarkers.ROLE)
    }

    override suspend fun unsubscribe(subscriptionId: AvValueSubscriptionId) {
        ensureHas(securityOfficer)
        valueWorker.unsubscribe(subscriptionId)
    }

}
