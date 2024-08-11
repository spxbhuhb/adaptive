/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class JvmLogger(name : String) : AdaptiveLogger {

    val platformLogger: Logger = LoggerFactory.getLogger(name)
    var println : Boolean = false

    override fun fine(message: String) {
        if (println) println("FINE $message")
        platformLogger.debug(message)
    }

    override fun fine(exception: Exception) {
        if (println) println("FINE $exception")
        platformLogger.debug(exception.message, exception)
    }

    override fun info(message: String) {
        if (println) println("INFO $message")
        platformLogger.info(message)
    }

    override fun warning(message: String) {
        if (println) println("WARNING $message")
        platformLogger.warn(message)
    }

    override fun warning(exception: Exception) {
        if (println) println("WARNING $exception")
        platformLogger.warn(exception.toString(), exception)
    }

    override fun warning(message: String, exception: Exception) {
        if (println) println("WARNING $message $exception")
        platformLogger.warn(message, exception)
    }

    override fun error(message: String) {
        if (println) println("ERROR $message")
        platformLogger.error(message)
    }

    override fun error(exception: Exception) {
        if (println) println("ERROR $exception")
        platformLogger.error(exception.toString(), exception)
    }

    override fun error(message: String, exception: Exception) {
        if (println) println("ERROR $message $exception")
        platformLogger.error(message, exception)
    }

    override fun fatal(message: String) : Nothing {
        if (println) println("FATAL $message")
        platformLogger.error(message)
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception) : Nothing  {
        if (println) println("FATAL $exception")
        platformLogger.error(exception.toString(), exception)
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception) : Nothing  {
        if (println) println("FATAL $message $exception")
        platformLogger.error(message, exception)
        exitProcessCommon(3210)
    }

}