/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

import kotlin.system.exitProcess

actual fun exitProcessCommon(status: Int) {
    exitProcess(status)
}