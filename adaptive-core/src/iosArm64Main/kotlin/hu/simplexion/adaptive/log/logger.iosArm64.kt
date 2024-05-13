/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

actual fun logger(name: String): AdaptiveLogger {
    return IOSLogger(name)
}