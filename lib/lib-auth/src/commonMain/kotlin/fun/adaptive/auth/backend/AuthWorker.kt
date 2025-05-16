/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.backend.basic.AuthBasicService
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.query.firstImplOrNull
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.firstItemOrNull
import `fun`.adaptive.value.AvValue

class AuthWorker : WorkerImpl<AuthWorker>() {

    lateinit var securityOfficer: AvValueId

    val valueWorker by workerImpl<AvValueWorker>()


    override suspend fun run() {
        getOrCreateSoRole()
        optCreateSoPrincipal()
    }

    fun getOrCreateSoRole() {
        val soRole = valueWorker.firstItemOrNull(AuthMarkers.ROLE) { AuthMarkers.SECURITY_OFFICER in it.markers }

        if (soRole == null) {
            valueWorker.queueAdd(
                AvValue(
                    name = AuthMarkers.SECURITY_OFFICER,
                    markersOrNull = setOf(AuthMarkers.ROLE, AuthMarkers.SECURITY_OFFICER),
                    spec = RoleSpec()
                ).also {
                    securityOfficer = it.uuid
                }
            )
        } else {
            securityOfficer = soRole.uuid.cast()
        }
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
                AvValue(
                    name = "Security Officer",
                    markersOrNull = setOf(AuthMarkers.BASIC_ACCOUNT),
                    spec = BasicAccountSpec(email = "so@localhost")
                )
            }

            getPrincipalService(securityOfficer).addPrincipal(
                "so",
                PrincipalSpec(activated = true, roles = setOf(securityOfficer)),
                PASSWORD,
                "so",
                account
            )
        }
    }

}