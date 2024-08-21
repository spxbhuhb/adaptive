/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class JvmLogger(name: String) : AdaptiveLogger {

    override var level: LogLevel = LogLevel.Info

    val platformLogger: Logger = LoggerFactory.getLogger(name)

    override fun fine(message: String) {
        if (level == LogLevel.Fine) {
            platformLogger.debug(message)
        }
    }

    override fun fine(exception: Exception) {
        if (level == LogLevel.Fine) {
            platformLogger.debug(exception.message, exception)
        }
    }

    override fun info(message: String) {
        if (level <= LogLevel.Info) {
            platformLogger.info(message)
        }
    }

    override fun warning(message: String) {
        if (level <= LogLevel.Warning) {
            platformLogger.warn(message)
        }
    }

    override fun warning(exception: Exception) {
        if (level <= LogLevel.Warning) {
            platformLogger.warn(exception.toString(), exception)
        }
    }

    override fun warning(message: String, exception: Exception) {
        if (level <= LogLevel.Warning) {
            platformLogger.warn(message, exception)
        }
    }

    override fun error(message: String) {
        if (level <= LogLevel.Error) {
            platformLogger.error(message)
        }
    }

    override fun error(exception: Exception) {
        if (level <= LogLevel.Error) {
            platformLogger.error(exception.toString(), exception)
        }
    }

    override fun error(message: String, exception: Exception) {
        if (level <= LogLevel.Error) {
            platformLogger.error(message, exception)
        }
    }

    override fun fatal(message: String): Nothing {
        platformLogger.error(message)
        exitProcessCommon(3210)
    }

    override fun fatal(exception: Exception): Nothing {
        platformLogger.error(exception.toString(), exception)
        exitProcessCommon(3210)
    }

    override fun fatal(message: String, exception: Exception): Nothing {
        platformLogger.error(message, exception)
        exitProcessCommon(3210)
    }

}