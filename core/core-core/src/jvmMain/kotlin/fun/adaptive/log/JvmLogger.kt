/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class JvmLogger(name: String) : AdaptiveLogger() {

    val platformLogger: Logger = LoggerFactory.getLogger(name)

    override fun rawFine(message: String?, throwable: Throwable?) {
        platformLogger.debug(message, throwable)
    }

    override fun rawInfo(message: String?, throwable: Throwable?) {
        platformLogger.info(message, throwable)
    }

    override fun rawWarning(message: String?, throwable: Throwable?) {
        platformLogger.warn(message, throwable)
    }

    override fun rawError(message: String?, throwable: Throwable?) {
        platformLogger.error(message, throwable)
    }

    override fun fatal(message: String, throwable: Throwable?): Nothing {
        platformLogger.error(message, throwable)
        exitProcessCommon(3210)
    }

}