/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvMarkerValue
import `fun`.adaptive.value.item.AvStatus
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

@Adat
data class AuthCredentialList(
    override val uuid: AvValueId,
    override val timestamp: Instant = now(),
    override val status: AvStatus = AvStatus.OK,
    override val parentId: AvValueId?,
    override var type: String,
    val credentials: Set<Credential>
) : AvMarkerValue() {

    override val markerName: String
        get() = "credentialList"

}