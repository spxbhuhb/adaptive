package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.*
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AuthModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + AccessDenied
        + AuthHistoryEntry
        + AuthenticationFail
        + AuthenticationResult
        + Credential
        + PrincipalSpec
        + RoleSpec
        + SecurityPolicy
        + Session
    }

    companion object {
        const val ALREADY_EXISTS = "already exists"
    }
}