/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

class BrowserLogger(
    name : String
) : AdaptiveLogger {

    val prefix = "[$name]"

    override fun fine(exception: Exception) {
        // TODO console.debug
        console.info(prefix, exception)
    }

    override fun info(message: String) {
        console.info(prefix, message)
    }

    override fun warning(exception: Exception) {
        console.warn(prefix, exception)
    }

    override fun error(exception: Exception) {
        console.error(prefix, exception)
    }

    override fun error(message: String, exception: Exception) {
        console.error(prefix, message, exception)
    }

}