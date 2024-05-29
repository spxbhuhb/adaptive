/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

import hu.simplexion.adaptive.utility.exitProcessCommon
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.Logger.getLogger

class AndroidLogger(name : String) : AdaptiveLogger {

    val platformLogger: Logger = getLogger(name)
    var println : Boolean = false

    override fun fine(message: String) {
        if (println) println("FINE $message")
        platformLogger.log(Level.FINE, message)
    }

    override fun fine(exception: Exception) {
        if (println) println("FINE $exception")
        platformLogger.log(Level.FINE, exception.message, exception)
    }

    override fun info(message: String) {
        if (println) println("INFO $message")
        platformLogger.log(Level.INFO, message)
    }

    override fun warning(message: String) {
        if (println) println("WARNING $message")
        platformLogger.log(Level.WARNING, message)
    }

    override fun warning(exception: Exception) {
        if (println) println("WARNING $exception")
        platformLogger.log(Level.WARNING, exception.toString(), exception)
    }

    override fun warning(message: String, exception: Exception) {
        if (println) println("WARNING $message $exception")
        platformLogger.log(Level.WARNING, message, exception)
    }

    override fun error(message: String) {
        if (println) println("ERROR $message")
        platformLogger.log(Level.SEVERE, message)
    }

    override fun error(exception: Exception) {
        if (println) println("ERROR $exception")
        platformLogger.log(Level.SEVERE, exception.toString(), exception)
    }

    override fun error(message: String, exception: Exception) {
        if (println) println("ERROR $message $exception")
        platformLogger.log(Level.SEVERE, message, exception)
    }

    override fun fatal(message: String) : Nothing {
        if (println) println("FATAL $message")
        platformLogger.log(Level.SEVERE, message)
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception) : Nothing  {
        if (println) println("FATAL $exception")
        platformLogger.log(Level.SEVERE, exception.toString(), exception)
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception) : Nothing  {
        if (println) println("FATAL $message $exception")
        platformLogger.log(Level.SEVERE, message, exception)
        exitProcessCommon(3210)
    }

}