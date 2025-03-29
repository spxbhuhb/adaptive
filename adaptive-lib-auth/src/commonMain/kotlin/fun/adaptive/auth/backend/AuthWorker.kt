/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.backend.basic.AuthBasicService
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.backend.query.firstImplOrNull
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.firstItemOrNull
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.persistence.AbstractValuePersistence
import `fun`.adaptive.value.persistence.NoPersistence

class AuthWorker : WorkerImpl<AuthWorker> {

    lateinit var securityOfficer: AvValueId

    val valueWorker by worker<AvValueWorker>()

    override fun mount() {
        getOrCreateSoRole()
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

    override suspend fun run() {
        optCreateSoPrincipal()
    }

    /**
     * Create a security officer principal if none exists. Also create a basic
     * account for the principal if `BasicAccountService` is part of the backend.
     */
    suspend fun optCreateSoPrincipal() {

        val soPrincipal = valueWorker.firstItemOrNull(AuthMarkers.PRINCIPAL) {
            securityOfficer in ((it.spec as? PrincipalSpec)?.roles ?: emptySet())
        }

        if (soPrincipal == null) {
            val account = safeAdapter.firstImplOrNull<AuthBasicService>()?.let {
                AvItem<BasicAccountSpec>(
                    name = "Security Officer",
                    type = AuthMarkers.BASIC_ACCOUNT,
                    friendlyId = "Security Officer",
                    spec = BasicAccountSpec(email = "so@localhost")
                )
            }

            getPrincipalService().addPrincipal(
                "so",
                PrincipalSpec(activated = true),
                PASSWORD,
                "so",
                account
            )
        }
    }

    fun getPrincipalService() =
        safeAdapter.firstImpl<AuthPrincipalService>().newInstance(Session.contextForRole(securityOfficer))

}