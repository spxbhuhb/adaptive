/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon

class BrowserLogger(
    name: String
) : AdaptiveLogger {

    override var level: LogLevel = LogLevel.Info

    val prefix = "[$name]"

    override fun fine(message: String) {
        // TODO console.debug
        if (level == LogLevel.Fine) {
            console.info(prefix, message)
        }
    }

    override fun fine(exception: Exception) {
        // TODO console.debug
        if (level == LogLevel.Fine) {
            console.info(prefix, exception)
        }
    }

    override fun info(message: String) {
        if (level <= LogLevel.Info) {
            console.info(prefix, message)
        }
    }

    override fun warning(message: String) {
        if (level <= LogLevel.Warning) {
            console.error(prefix, message)
        }
    }

    override fun warning(exception: Exception) {
        if (level <= LogLevel.Warning) {
            console.error(prefix, exception)
        }
    }

    override fun warning(message: String, exception: Exception) {
        if (level <= LogLevel.Warning) {
            console.error(prefix, message, exception)
        }
    }

    override fun error(message: String) {
        if (level <= LogLevel.Error) {
            console.error(prefix, message)
        }
    }

    override fun error(exception: Exception) {
        if (level <= LogLevel.Error) {
            console.error(prefix, exception)
        }
    }

    override fun error(message: String, exception: Exception) {
        if (level <= LogLevel.Error) {
            console.error(prefix, message, exception)
        }
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