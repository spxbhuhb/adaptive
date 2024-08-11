/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

interface AdaptiveLogger {

    fun fine(message: String)

    fun fine(exception: Exception)

    fun info(message: String)

    fun warning(message : String)

    fun warning(exception: Exception)

    fun warning(message : String, exception: Exception)

    fun error(message : String)

    fun error(exception: Exception)

    fun error(message : String, exception: Exception)

    fun fatal(message: String) : Nothing

    fun fatal(exception: Exception) : Nothing

    fun fatal(message : String, exception: Exception) : Nothing

}