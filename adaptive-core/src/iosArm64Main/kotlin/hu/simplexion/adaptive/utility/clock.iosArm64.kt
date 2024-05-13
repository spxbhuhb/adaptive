/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

/**
 * Returns with a number that represents virtual machine time in microseconds.
 * The number has no relation to clock time. The difference between two returned
 * values represents the number of microseconds elapsed between the two calls.
 *
 * On JVM it uses `System.nanoTime`.
 */
actual fun vmNowMicro(): Long {
    TODO("Not yet implemented")
}

actual fun vmNowSecond(): Long {
    TODO("Not yet implemented")
}

actual fun sleep(millis: Long) {
    TODO("Not yet implemented")
}