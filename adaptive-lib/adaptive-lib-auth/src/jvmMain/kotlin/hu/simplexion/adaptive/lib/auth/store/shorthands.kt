/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.AuthHistoryEntry
import hu.simplexion.adaptive.auth.model.Principal
import hu.simplexion.adaptive.auth.model.Role
import hu.simplexion.adaptive.call.CallSiteName
import hu.simplexion.adaptive.lib.auth.context.getPrincipal
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock

// these shorthands are internal so I don't pollute namespace

internal val credentials = CredentialTable
internal val history = HistoryTable
internal val principals = PrincipalTable
internal val roleContexts = RoleContextTable
internal val roleGrants = RoleGrantTable
internal val roleMemberships = RoleMembershipTable
internal val roles = RoleTable

@CallSiteName
internal fun ServiceImpl<*>.history(
    role: Role,
    callSiteFqName: String = ""
) =
    add(callSiteFqName, null, role.id)


@CallSiteName
internal fun ServiceImpl<*>.history(
    role: UUID<Role>,
    callSiteFqName: String = ""
) =
    add(callSiteFqName, null, role)

@CallSiteName
internal fun ServiceImpl<*>.history(
    principal: UUID<Principal>?,
    role: UUID<Role>?,
    callSiteFqName: String = ""
) =
    add(callSiteFqName, principal, role)


@CallSiteName
internal fun ServiceImpl<*>.history(
    role: UUID<Role>? = null,
    roleGroup: UUID<Role>? = null,
    callSiteFqName: String = ""
) =
    add(callSiteFqName, null, role, roleGroup)

private fun ServiceImpl<*>.add(
    function: String,
    principal: UUID<Principal>? = null,
    role: UUID<Role>? = null,
    roleGroup: UUID<Role>? = null
) {
    history.add {
        AuthHistoryEntry(
            UUID(),
            function,
            serviceContext.getPrincipal(),
            Clock.System.now(),
            principal,
            role,
            roleGroup
        )
    }
}