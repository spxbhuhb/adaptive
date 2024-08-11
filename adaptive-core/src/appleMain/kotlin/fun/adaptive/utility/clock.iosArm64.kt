/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.utility

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

actual fun vmNowMicro(): Long =
    startTime.elapsedNow().inWholeMicroseconds

actual fun vmNowSecond(): Long =
    startTime.elapsedNow().inWholeSeconds

@OptIn(ExperimentalForeignApi::class)
actual fun sleep(millis: Long) {
    platform.posix.sleep(millis.toUInt().convert())
}