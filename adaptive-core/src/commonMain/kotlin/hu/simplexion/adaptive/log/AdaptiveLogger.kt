/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

interface AdaptiveLogger {

    fun fine(exception: Exception)

    fun info(message: String)

    fun warning(exception: Exception)

    fun error(exception: Exception)

    fun error(message : String, exception: Exception)

}