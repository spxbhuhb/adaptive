/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.app.basic.auth

import `fun`.adaptive.app.basic.auth.model.BasicAccount
import `fun`.adaptive.auth.model.*
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.store
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.lib.auth.authJvm
import `fun`.adaptive.lib.auth.crypto.BCrypt
import `fun`.adaptive.lib.auth.service.PrincipalService
import `fun`.adaptive.lib.auth.service.RoleService
import `fun`.adaptive.lib.auth.store.*
import `fun`.adaptive.utility.UUID
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction

@Adaptive
fun authBasicJvm() {
    authJvm()

    store { BasicAccountTable }
    service { BasicAccountService() }
}

object AuthBasicInit {

    fun role(name: String): Role {
        val existing = RoleTable.getByNameOrNull(name)
        if (existing != null) return existing

        val new = Role(UUID(), name)
        RoleTable += new

        return new
    }

    fun account(accountName: String, ownerName: String, password: String): Principal {

        val existing = PrincipalTable.getByNameOrNull(accountName)
        if (existing != null) return existing

        val principal = Principal(UUID(), accountName, activated = true)
        PrincipalTable += principal

        val account = BasicAccount(principal.id.cast(), ownerName, "", "")
        BasicAccountTable += account

        val passwd = BCrypt.hashpw(password, BCrypt.gensalt())
        val credential = Credential(UUID(), principal.id, CredentialType.PASSWORD, passwd, now())
        CredentialTable += credential

        return principal
    }

    fun grant(role: Role, principal: Principal) {

        if (RoleGrantTable.hasRole(role.id, principal.id)) return

        RoleGrantTable += RoleGrant(principal.id, role.id)
    }

    fun authBasicInit(init: AuthBasicInit.() -> Unit) {
        transaction {
            init()
        }
    }

    fun authBasicDefault(password: String) {

        authBasicInit {
            val soRole = role("security-officer")
            val soPrincipal = account("so", "Security Officer", password)
            grant(soRole, soPrincipal)

            PrincipalService.addRoles += soRole.id
            PrincipalService.getRoles += soRole.id
            PrincipalService.updateRoles += soRole.id

            RoleService.addRoles += soRole.id
            RoleService.getRoles += soRole.id
            RoleService.updateRoles += soRole.id
        }

    }

}