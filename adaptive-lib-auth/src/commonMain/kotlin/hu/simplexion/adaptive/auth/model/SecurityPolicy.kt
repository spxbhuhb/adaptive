/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
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
) : AdatClass<SecurityPolicy> {
    companion object : AdatCompanion<SecurityPolicy>
}