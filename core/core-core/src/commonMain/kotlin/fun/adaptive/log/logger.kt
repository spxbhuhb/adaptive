/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

var defaultLoggerFactory = LoggerFactory { getPlatformLogger(it) }

fun getLogger(name: String): AdaptiveLogger {
    return defaultLoggerFactory.getLogger(name)
}