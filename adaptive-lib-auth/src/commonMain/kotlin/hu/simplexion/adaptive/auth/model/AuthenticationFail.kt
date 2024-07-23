package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.service.model.ReturnException

/**
 * Thrown on authentication fail.
 */
@Adat
class AuthenticationFail(
    val result: AuthenticationResult
) : ReturnException(), AdatClass<AuthenticationFail> {
    companion object : AdatCompanion<AuthenticationFail>
}