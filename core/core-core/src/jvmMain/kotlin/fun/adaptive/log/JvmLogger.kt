/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

import `fun`.adaptive.utility.exitProcessCommon
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class JvmLogger(name: String) : AdaptiveLogger() {

    val platformLogger: Logger = LoggerFactory.getLogger(name)

    override fun rawFine(message: String?, exception: Exception?) {
        platformLogger.debug(message, exception)
    }

    override fun rawInfo(message: String?, exception: Exception?) {
        platformLogger.info(message, exception)
    }

    override fun rawWarning(message: String?, exception: Exception?) {
        platformLogger.warn(message, exception)
    }

    override fun rawError(message: String?, exception: Exception?) {
        platformLogger.error(message, exception)
    }

    override fun fatal(message: String, exception: Exception?): Nothing {
        platformLogger.error(message, exception)
        exitProcessCommon(3210)
    }

}