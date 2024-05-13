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
expect fun vmNowMicro(): Long

expect fun vmNowSecond() : Long

expect fun sleep(millis: Long)