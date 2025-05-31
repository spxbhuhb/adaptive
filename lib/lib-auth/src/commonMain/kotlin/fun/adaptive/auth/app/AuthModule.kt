package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.*
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AuthModule<FW : AbstractWorkspace, BW: BackendWorkspace> : AppModule<FW,BW>() {

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