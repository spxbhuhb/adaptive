/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

import hu.simplexion.adaptive.utility.exitProcessCommon

class BrowserLogger(
    name: String
) : AdaptiveLogger {

    val prefix = "[$name]"

    override fun fine(message: String) {
        // TODO console.debug
        console.info(prefix, message)
    }

    override fun fine(exception: Exception) {
        // TODO console.debug
        console.info(prefix, exception)
    }

    override fun info(message: String) {
        console.info(prefix, message)
    }

    override fun warning(message: String) {
        console.error(prefix, message)
    }

    override fun warning(exception: Exception) {
        console.error(prefix, exception)
    }

    override fun warning(message: String, exception: Exception) {
        console.error(prefix, message, exception)
    }


    override fun error(message: String) {
        console.error(prefix, message)
    }

    override fun error(exception: Exception) {
        console.error(prefix, exception)
    }

    override fun error(message: String, exception: Exception) {
        console.error(prefix, message, exception)
    }

    override fun fatal(message: String): Nothing {
        println("FATAL: [$prefix] $message")
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception): Nothing {
        println("FATAL: [$prefix] ${exception.stackTraceToString()}")
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception): Nothing {
        println("FATAL: [$prefix] $message ${exception.stackTraceToString()}")
        exitProcessCommon(3210)
    }

}