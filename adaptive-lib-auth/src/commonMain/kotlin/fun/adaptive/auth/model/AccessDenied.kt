/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.service.model.ReturnException

/**
 * Thrown when an access check fails for an authenticated/unauthenticated session.
 */
@Adat
class AccessDenied : ReturnException(), AdatClass<AccessDenied> {
    companion object : AdatCompanion<AccessDenied>
}