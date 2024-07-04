/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.utility.UUID
import kotlin.time.Duration

@Adat
class SecurityPolicy(
    var twoFactorAuthentication: Boolean,
    var sessionActivationInterval: Duration,
    var sessionExpirationInterval: Duration,
    var passwordChangeInterval: Duration,
    var passwordHistoryLength: Int,
    var passwordLengthMinimum: Int,
    var uppercaseMinimum: Int,
    var digitMinimum: Int,
    var specialCharacterMinimum: Int,
    var sameCharacterMaximum: Int,
    var minEntropy: EntropyCategory,
    var maxFailedAuths: Int
)