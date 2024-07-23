/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.service.model.ReturnException

/**
 * Thrown when an access check fails for an authenticated/unauthenticated session.
 */
@Adat
class AccessDenied : ReturnException(), AdatClass<AccessDenied> {
    companion object : AdatCompanion<AccessDenied>
}