/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

class InvalidAdatException : RuntimeException()

fun ensureValid(data: AdatClass<*>) {
    // FIXME if (! data.isValid()) throw InvalidAdatException()
}