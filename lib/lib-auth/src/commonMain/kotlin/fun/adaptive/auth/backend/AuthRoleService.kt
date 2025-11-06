/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.service.auth.ensureHas
import `fun`.adaptive.service.auth.ensureLoggedIn
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.util.info
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceProvider
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.asAvValue
import `fun`.adaptive.value.util.serviceSubscribe

@ServiceProvider
class AuthRoleService : AuthRoleApi, ServiceImpl<AuthRoleService>() {

    val valueWorker by workerImpl<AvValueWorker>()
    val authWorker by workerImpl<AuthWorker>()

    val securityOfficer
        get() = authWorker.securityOfficer

    override suspend fun all(): List<AuthRole> {
        ensureLoggedIn()

        return valueWorker.queryByMarker(AuthMarkers.ROLE).map {
            it.asAvValue<RoleSpec>()
        }
    }

    override suspend fun save(roleId: AvValueId?, name: String, spec: RoleSpec) {
        ensureHas(securityOfficer)

        info { "save($roleId, $name, $spec)" }

        if (roleId == null) {
            add(name, spec)
        } else {
            update(roleId, name, spec)
        }
    }

    private suspend fun add(name: String, spec: RoleSpec) {

        val roleValue = AvValue(
            name = name,
            markersOrNull = setOf(AuthMarkers.ROLE),
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
        valueWorker.update<RoleSpec>(roleId) {
            it.copy(name = name, spec = spec)
        }
    }

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition> {
        ensureHas(securityOfficer)
        return serviceSubscribe(valueWorker, subscriptionId, AuthMarkers.ROLE)
    }

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId) {
        ensureHas(securityOfficer)
        valueWorker.unsubscribe(subscriptionId)
    }

}
