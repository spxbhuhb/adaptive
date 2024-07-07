/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.auth.util

import kotlin.math.log

fun calculatePasswordEntropy(password: String): Double {
    val characterSetSize = calculateCharacterSetSize(password)
    val passwordLength = password.length
    return passwordLength * log(characterSetSize.toDouble(), 2.0)
}

fun calculateCharacterSetSize(password: String): Long {
    // Determine the character set size based on the types of characters used in the password.
    var characterSetSize = 0L

    // Check if the password contains lowercase letters
    if (password.any { it.isLowerCase() }) {
        characterSetSize += 26
    }

    // Check if the password contains uppercase letters
    if (password.any { it.isUpperCase() }) {
        characterSetSize += 26
    }

    // Check if the password contains digits
    if (password.any { it.isDigit() }) {
        characterSetSize += 10
    }

    // Check if the password contains special characters (e.g., !@#$%^&*()-_=+[]{}|;:,.<>?)
    if (password.any { it.isLetterOrDigit().not() }) {
        characterSetSize += 32 // Assuming 32 common special characters
    }

    // If the password is entirely composed of one character type, reduce the character set size.
    if (password.all { it.isLowerCase() } || password.all { it.isUpperCase() } || password.all { it.isDigit() }) {
        characterSetSize = 1
    }

    return characterSetSize
}
