/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

import hu.simplexion.adaptive.utility.exitProcessCommon

// FIXME IOS logger
class IOSLogger(
    name: String
) : AdaptiveLogger {

    val prefix = "[$name]"

    override fun fine(exception: Exception) {
        println("FINE: [$prefix] ${exception.stackTraceToString()}")
    }

    override fun info(message: String) {
        println("INFO: $prefix $message")
    }

    override fun warning(message: String) {
        println("WARNING: [$prefix] $message")
    }

    override fun warning(exception: Exception) {
        println("WARNING: [$prefix] ${exception.stackTraceToString()}")
    }

    override fun warning(message: String, exception: Exception) {
        println("WARNING: [$prefix] $message ${exception.stackTraceToString()}")
    }

    override fun error(message: String) {
        println("ERROR: [$prefix] $message")
    }

    override fun error(exception: Exception) {
        println("ERROR: [$prefix] ${exception.stackTraceToString()}")
    }

    override fun error(message: String, exception: Exception) {
        println("ERROR: [$prefix] $message ${exception.stackTraceToString()}")
    }

    override fun fatal(message: String) : Nothing {
        println("FATAL: [$prefix] $message")
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception) : Nothing {
        println("FATAL: [$prefix] ${exception.stackTraceToString()}")
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception) : Nothing {
        println("FATAL: [$prefix] $message ${exception.stackTraceToString()}")
        exitProcessCommon(3210)
    }

}