/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValueId
import kotlin.time.Instant

@Adat
data class PrincipalSpec(
    val activated: Boolean = false,
    val locked: Boolean = false,
    val expired: Boolean = false,
    val anonymized: Boolean = false,
    val lastAuthSuccess: Instant? = null,
    val authSuccessCount: Int = 0,
    val lastAuthFail: Instant? = null,
    val authFailCount: Int = 0,
    val roles: Set<AvValueId> = emptySet()
)