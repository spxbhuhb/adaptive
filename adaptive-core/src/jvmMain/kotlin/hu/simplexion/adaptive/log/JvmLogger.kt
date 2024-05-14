/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.log

import hu.simplexion.adaptive.utility.exitProcessCommon
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.Logger.getLogger


class JvmLogger(name : String) : AdaptiveLogger {

    val platformLogger: Logger = getLogger(name)

    override fun fine(exception: Exception) {
        platformLogger.log(Level.FINE, exception.message, exception)
    }

    override fun info(message: String) {
        platformLogger.log(Level.INFO, message)
    }

    override fun warning(message: String) {
        platformLogger.log(Level.WARNING, message)
    }

    override fun warning(exception: Exception) {
        platformLogger.log(Level.WARNING, exception.toString(), exception)
    }

    override fun warning(message: String, exception: Exception) {
        platformLogger.log(Level.WARNING, message, exception)
    }

    override fun error(message: String) {
        platformLogger.log(Level.SEVERE, message)
    }

    override fun error(exception: Exception) {
        platformLogger.log(Level.SEVERE, exception.toString(), exception)
    }

    override fun error(message: String, exception: Exception) {
        platformLogger.log(Level.SEVERE, message, exception)
    }

    override fun fatal(message: String) : Nothing {
        platformLogger.log(Level.SEVERE, message)
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception) : Nothing  {
        platformLogger.log(Level.SEVERE, exception.toString(), exception)
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception) : Nothing  {
        platformLogger.log(Level.SEVERE, message, exception)
        exitProcessCommon(3210)
    }

}