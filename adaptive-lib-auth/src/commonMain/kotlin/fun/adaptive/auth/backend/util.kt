package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.backend.builtin.BackendFragmentImpl
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.value.AvValueId

fun BackendFragmentImpl.getPrincipalService(role : AvValueId) =
    safeAdapter.firstImpl<AuthPrincipalService>().newInstance(Session.contextForRole(role))
