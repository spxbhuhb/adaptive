package `fun`.adaptive.auth.app

import `fun`.adaptive.auth.model.*
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.value.operation.*
import `fun`.adaptive.wireformat.WireFormatRegistry

open class AuthModule<WT, AT : Any> : AppModule<WT, AT>() {

    override fun WireFormatRegistry.init() {
        this += AccessDenied
        this += CredentialList
        this += AuthHistoryEntry
        this += AuthenticationFail
        this += AuthenticationResult
        this += Credential
        this += PrincipalSpec
        this += RoleSpec
        this += SecurityPolicy
        this += Session
    }
    
}