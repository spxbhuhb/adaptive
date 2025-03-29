/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.firstItemOrNull
import `fun`.adaptive.value.item.AvItem

class AuthWorker : WorkerImpl<AuthWorker> {

    companion object {
        lateinit var valueWorker: AvValueWorker

        lateinit var securityOfficer: AuthRoleId
            private set
    }

    override fun mount() {
        valueWorker = safeAdapter.firstImpl<AvValueWorker>()
        getOrCreateSoRole()
        optCreateSoPrincipal()
    }

    fun getOrCreateSoRole() {
        val soRole = valueWorker.firstItemOrNull(AuthMarkers.ROLE) { AuthMarkers.SECURITY_OFFICER in it }

        if (soRole == null) {
            valueWorker.queueAdd(
                AvItem(
                    name = AuthMarkers.SECURITY_OFFICER,
                    type = AUTH_ROLE,
                    parentId = null,
                    markersOrNull = mutableMapOf(
                        AuthMarkers.ROLE to null,
                        AuthMarkers.SECURITY_OFFICER to null
                    ),
                    friendlyId = AuthMarkers.SECURITY_OFFICER,
                    spec = RoleSpec()
                ).also {
                    securityOfficer = it.uuid
                }
            )
        } else {
            securityOfficer = soRole.uuid.cast()
        }
    }

    fun optCreateSoPrincipal() {
        val soPrincipal = valueWorker.firstItemOrNull(AuthMarkers.PRINCIPAL) {
            securityOfficer in ((it.spec as? PrincipalSpec)?.roles ?: emptySet())
        }

        if (soPrincipal != null) return

        val (principalValue, credentialListValue) =
            AuthPrincipalService().addPrincipalPrep("so", PrincipalSpec(), PASSWORD, "so")

        valueWorker.queueAdd(principalValue)
        valueWorker.queueAdd(credentialListValue)

        // history -- added principal
    }

    override suspend fun run() {
        // auth worker does not do much atm, it is responsible for initialization mostly
    }


}