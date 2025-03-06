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
    r += Principal
    r += Role
    r += RoleContext
    r += RoleGrant
    r += RoleMembership
    r += SecurityPolicy
    r += Session
}