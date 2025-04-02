package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType.PASSWORD
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.value.AvValueId
import kotlinx.datetime.Clock.System.now

fun BackendFragmentImpl.getPrincipalService(role : AvValueId) =
    safeAdapter.firstImpl<AuthPrincipalService>().newInstance(Session.contextForRole(role))

fun BackendFragmentImpl.getSessionService(role: AvValueId) =
    safeAdapter.firstImpl<AuthSessionService>().newInstance(Session.contextForRole(role))

internal fun Credential.hash(type: String? = null) =
    Credential(type ?: PASSWORD, BCrypt.hashpw(value, BCrypt.gensalt()), now())

