package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now

fun BackendFragmentImpl.getPrincipalService(roleId: AvValueId) =
    safeAdapter.firstImpl<AuthPrincipalService>().newInstance(Session.contextFor(roleId = roleId))

fun BackendFragmentImpl.getSessionService(roleId: AvValueId) =
    safeAdapter.firstImpl<AuthSessionService>().newInstance(Session.contextFor(roleId = roleId))

internal fun Credential.hash(type: String? = null) =
    Credential(type ?: PASSWORD, BCrypt.hashpw(value, BCrypt.gensalt()), now())

