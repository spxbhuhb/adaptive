/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Instant

@Adat
class AuthHistoryEntry(

    val uuid: UUID<AuthHistoryEntry>,

    val event: String,
    val executedBy: AuthPrincipalId?,
    val executedAt: Instant,

    val session: UUID<Session>?,
    val principal: AuthPrincipalId?,
    val role: AuthRoleId?,
    val roleGroup: AuthRoleId?,

    val result: AuthenticationResult?

)