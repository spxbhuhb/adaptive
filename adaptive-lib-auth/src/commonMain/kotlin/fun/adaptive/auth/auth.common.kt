package `fun`.adaptive.auth

import `fun`.adaptive.auth.model.*
import `fun`.adaptive.wireformat.WireFormatRegistry

fun authCommon() {
    val r = WireFormatRegistry

    r += AuthenticationResult
    r += AccessDenied
    r += AuthenticationFail
    r += AuthHistoryEntry
    r += Credential
    r += PrincipalSpec
    r += RoleSpec
    r += SecurityPolicy
    r += Session
}