/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon
import platform.Foundation.NSLog

class IOSLogger(
    name: String,
) : AdaptiveLogger {

    override var level = LogLevel.Info
    val prefix = "[$name]"

    private inline fun log(messageLevel: LogLevel, message: () -> String) {
        if (level <= messageLevel) NSLog(message().replace("%", "%%"))
    }

    override fun fine(message: String) {
        log(LogLevel.Fine) { "FINE: [$prefix] $message" }
    }

    override fun fine(exception: Exception) {
        log(LogLevel.Fine) { "FINE: [$prefix] ${exception.stackTraceToString()}" }
    }

    override fun info(message: String) {
        log(LogLevel.Info) { "INFO: [$prefix] $message" }
    }

    override fun warning(message: String) {
        log(LogLevel.Warning) { "WARNING: [$prefix] $message" }
    }

    override fun warning(exception: Exception) {
        log(LogLevel.Warning) { "WARNING: [$prefix] ${exception.stackTraceToString()}" }
    }

    override fun warning(message: String, exception: Exception) {
        log(LogLevel.Warning) { "WARNING: [$prefix] $message ${exception.stackTraceToString()}" }
    }

    override fun error(message: String) {
        log(LogLevel.Error) { "ERROR: [$prefix] $message" }
    }

    override fun error(exception: Exception) {
        log(LogLevel.Error) { "ERROR: [$prefix] ${exception.stackTraceToString()}" }
    }

    override fun error(message: String, exception: Exception) {
        log(LogLevel.Error) { "ERROR: [$prefix] $message ${exception.stackTraceToString()}" }
    }

    override fun fatal(message: String) : Nothing {
        log(LogLevel.Fatal) { "FATAL: [$prefix] $message" }
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception) : Nothing {
        log(LogLevel.Fatal) { "FATAL: [$prefix] ${exception.stackTraceToString()}" }
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception) : Nothing {
        log(LogLevel.Fatal) { "FATAL: [$prefix] $message ${exception.stackTraceToString()}" }
        exitProcessCommon(3210)
    }

}