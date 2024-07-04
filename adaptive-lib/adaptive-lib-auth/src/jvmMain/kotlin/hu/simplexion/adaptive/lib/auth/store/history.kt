/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.auth.store

import hu.simplexion.adaptive.auth.model.*
import hu.simplexion.adaptive.lib.auth.context.getPrincipal
import hu.simplexion.adaptive.lib.auth.context.getSessionOrNull
import hu.simplexion.adaptive.server.builtin.ServiceImpl
import hu.simplexion.adaptive.utility.CallSiteName
import hu.simplexion.adaptive.utility.UUID
import kotlinx.datetime.Clock

@CallSiteName
internal fun ServiceImpl<*>.history(
    role: Role,
    callSiteName: String = ""
) =
    add(callSiteName, null, role.id)

@CallSiteName
internal fun ServiceImpl<*>.history(
    role: UUID<Role>,
    callSiteName: String = ""
) =
    add(callSiteName, null, role)

@CallSiteName
internal fun ServiceImpl<*>.history(
    principal: UUID<Principal>?,
    role: UUID<Role>? = null,
    callSiteName: String = ""
) =
    add(callSiteName, principal, role)

@CallSiteName
internal fun ServiceImpl<*>.history(
    principal: UUID<Principal>,
    result: AuthenticationResult,
    callSiteName: String = ""
) =
    add(callSiteName, principal, result = result)

@CallSiteName
internal fun ServiceImpl<*>.history(
    role: UUID<Role>? = null,
    roleGroup: UUID<Role>? = null,
    callSiteName: String = ""
) =
    add(callSiteName, null, role, roleGroup)

private fun ServiceImpl<*>.add(
    function: String,
    principal: UUID<Principal>? = null,
    role: UUID<Role>? = null,
    roleGroup: UUID<Role>? = null,
    result: AuthenticationResult? = null
) {
    history.add {
        AuthHistoryEntry(
            UUID(),
            function,
            serviceContext.getPrincipal(),
            Clock.System.now(),
            serviceContext.getSessionOrNull()?.id,
            principal,
            role,
            roleGroup,
            result
        )
    }
}


@CallSiteName
internal fun history(
    principal: UUID<Principal>,
    session: UUID<Session>? = null,
    callSiteName: String = ""
) {
    history.add {
        AuthHistoryEntry(
            UUID(),
            callSiteName,
            principal,
            Clock.System.now(),
            session,
            principal,
            null,
            null,
            null
        )
    }
}
