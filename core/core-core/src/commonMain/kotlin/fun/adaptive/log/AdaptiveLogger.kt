/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.log

abstract class AdaptiveLogger {

    var usePrintln : Boolean = false

    var level: LogLevel = LogLevel.Info

    /**
     * Disconnect from this logger when it is no longer used.
     */
    fun disconnect() {
        // FIXME logger disconnect
    }

    // --------------------------------------------------------------------------------
    // Fine (a.k.a. debug, trace)
    // --------------------------------------------------------------------------------

    abstract fun rawFine(message: String? = null, exception: Exception? = null)

    fun fine(message: String) {
        if (level == LogLevel.Fine) rawFine(message)
    }

    fun fine(exception: Exception) {
        if (level == LogLevel.Fine) rawFine(exception = exception)
    }

    fun fine(message: String? = null, exception: Exception? = null) {
        if (level == LogLevel.Fine) rawFine(message, exception)
    }

    inline fun fine(message: () -> String) {
        if (level == LogLevel.Fine) rawFine(message())
    }

    // --------------------------------------------------------------------------------
    // Info
    // --------------------------------------------------------------------------------

    abstract fun rawInfo(message: String? = null, exception: Exception? = null)

    fun info(message: String) {
        if (level <= LogLevel.Info) rawInfo(message)
    }

    fun info(exception: Exception) {
        if (level <= LogLevel.Info) rawInfo(exception = exception)
    }

    fun info(message: String? = null, exception: Exception? = null) {
        if (level <= LogLevel.Info) rawInfo(message, exception)
    }

    inline fun info(message: () -> String) {
        if (level <= LogLevel.Info) rawInfo(message())
    }

    // --------------------------------------------------------------------------------
    // Warning
    // --------------------------------------------------------------------------------

    abstract fun rawWarning(message: String? = null, exception: Exception? = null)

    fun warning(message: String) {
        if (level <= LogLevel.Warning) rawWarning(message)
    }

    fun warning(exception: Exception) {
        if (level <= LogLevel.Warning) rawWarning(exception = exception)
    }

    fun warning(message: String, exception: Exception) {
        if (level <= LogLevel.Warning) rawWarning(message, exception)
    }

    inline fun warning(exception: Exception? = null, message: () -> String) {
        if (level <= LogLevel.Warning) rawWarning(message(), exception)
    }

    inline fun AdaptiveLogger.warning(message: () -> String) {
        if (level <= LogLevel.Warning) warning(message())
    }

    // --------------------------------------------------------------------------------
    // Error
    // --------------------------------------------------------------------------------

    abstract fun rawError(message: String? = null, exception: Exception? = null)

    fun error(message: String) {
        if (level <= LogLevel.Error) rawError(message)
    }

    inline fun error(exception: Exception, message: () -> String) {
        error(message(), exception)
    }

    fun error(exception: Exception) {
        if (level <= LogLevel.Error) rawError(exception = exception)
    }

    fun error(message: String, exception: Exception) {
        if (level <= LogLevel.Error) rawError(message, exception)
    }

    inline fun error(message: () -> String) {
        if (level <= LogLevel.Error) error(message())
    }

    // --------------------------------------------------------------------------------
    // Fatal
    // --------------------------------------------------------------------------------

    abstract fun fatal(message: String, exception: Exception? = null): Nothing

    fun enableFine(): AdaptiveLogger {
        level = LogLevel.Fine
        return this
    }

}