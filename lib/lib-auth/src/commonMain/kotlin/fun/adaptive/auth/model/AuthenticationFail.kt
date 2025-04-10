package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.service.model.ReturnException

/**
 * Thrown on authentication fail.
 */
@Adat
class AuthenticationFail(
    val result: AuthenticationResult
) : ReturnException()