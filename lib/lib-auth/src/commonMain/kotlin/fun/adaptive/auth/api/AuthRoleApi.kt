/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.api

import `fun`.adaptive.auth.model.AuthRole
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.service.ServiceApi
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvSubscriptionId
import `fun`.adaptive.value.remote.AvPublisher

@ServiceApi
interface AuthRoleApi : AvPublisher {

    suspend fun all(): List<AuthRole>

    suspend fun save(roleId: AvValueId?, name: String, spec: RoleSpec)

    override suspend fun subscribe(subscriptionId: AvSubscriptionId): List<AvSubscribeCondition>

    override suspend fun unsubscribe(subscriptionId: AvSubscriptionId)

}