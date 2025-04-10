/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.isValid

class InvalidAdatException : RuntimeException()

fun ensureValid(data: AdatClass) {
    if (! data.isValid()) throw InvalidAdatException()
}