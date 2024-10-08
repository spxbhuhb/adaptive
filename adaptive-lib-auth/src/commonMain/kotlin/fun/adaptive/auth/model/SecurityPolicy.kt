/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

import `fun`.adaptive.adat.Adat
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Adat
class SecurityPolicy(
    var twoFactorAuthentication: Boolean = false,
    var sessionActivationInterval: Duration = 5.minutes,
    var sessionExpirationInterval: Duration = 30.minutes,
    var passwordChangeInterval: Duration = 30.minutes,
    var passwordHistoryLength: Int = 10,
    var passwordLengthMinimum: Int = 0,
    var uppercaseMinimum: Int = 0,
    var digitMinimum: Int = 0,
    var specialCharacterMinimum: Int = 0,
    var sameCharacterMaximum: Int = 0,
    var minEntropy: EntropyCategory = EntropyCategory.Poor,
    var maxFailedAuths: Int = 5
)