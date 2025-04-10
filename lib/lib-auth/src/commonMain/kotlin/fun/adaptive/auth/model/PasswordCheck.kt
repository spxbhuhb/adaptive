/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

enum class PasswordCheck {
    Length,
    Uppercase,
    Digit,
    Special,
    Same,
    Strength
}