/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.wireformat.builtin.EnumWireFormat

enum class AuthenticationResult {
    UnknownPrincipal,
    UnknownSession,
    InvalidSecurityCode,
    NoCredential,
    NotActivated,
    Locked,
    Expired,
    Anonymized,
    InvalidCredentials,
    Success,
    SecondFactorSuccess;

    companion object : EnumWireFormat<AuthenticationResult>(entries) {
        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.auth.model.AuthenticationResult"
    }
}