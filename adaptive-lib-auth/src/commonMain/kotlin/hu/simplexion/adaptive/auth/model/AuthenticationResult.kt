/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

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
    SecondFactorSuccess
}