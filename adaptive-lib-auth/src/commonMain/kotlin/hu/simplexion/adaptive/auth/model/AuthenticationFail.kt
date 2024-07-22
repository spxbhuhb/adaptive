package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.service.model.ReturnException

/**
 * Thrown on authentication fail.
 */
class AuthenticationFail(
    val result: AuthenticationResult
) : ReturnException(result.name)