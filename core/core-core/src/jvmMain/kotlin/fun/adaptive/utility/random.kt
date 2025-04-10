/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.utility

import java.security.SecureRandom

val generator = SecureRandom()

actual fun secureRandom(count: Int): IntArray {
    val array = IntArray(count)
    for (i in 0 until count) {
        array[i] = generator.nextInt()
    }
    return array
}