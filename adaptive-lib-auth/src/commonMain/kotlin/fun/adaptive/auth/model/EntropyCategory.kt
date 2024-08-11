/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.auth.model

enum class EntropyCategory(
    val min: Int,
    val max: Int
) {
    Poor(0, 24),
    Weak(25, 49),
    Reasonable(50, 74),
    Excellent(75, 100)
}