package hu.simplexion.adaptive.auth.util

import hu.simplexion.adaptive.auth.model.AuthenticationResult

/**
 * Thrown on authentication fail.
 */
class AuthenticationFail(
    val result: AuthenticationResult
) : RuntimeException(result.name)