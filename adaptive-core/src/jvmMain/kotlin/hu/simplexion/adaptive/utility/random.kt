/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.utility

import java.security.SecureRandom

val generator = SecureRandom()

actual fun fourRandomInt(): IntArray {
    val array = IntArray(4)
    array[0] = generator.nextInt()
    array[1] = generator.nextInt()
    array[2] = generator.nextInt()
    array[3] = generator.nextInt()
    return array
}