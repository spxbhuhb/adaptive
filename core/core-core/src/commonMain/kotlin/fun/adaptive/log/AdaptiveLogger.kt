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

    abstract fun rawFine(message: String? = null, throwable: Throwable? = null)

    fun fine(message: String) {
        if (level == LogLevel.Fine) rawFine(message)
    }

    fun fine(throwable: Throwable) {
        if (level == LogLevel.Fine) rawFine(throwable.message, throwable)
    }

    fun fine(message: String? = null, throwable: Throwable? = null) {
        if (level == LogLevel.Fine) rawFine(message, throwable)
    }

    inline fun fine(message: () -> String) {
        if (level == LogLevel.Fine) rawFine(message())
    }

    // --------------------------------------------------------------------------------
    // Info
    // --------------------------------------------------------------------------------

    abstract fun rawInfo(message: String? = null, throwable: Throwable? = null)

    fun info(message: String) {
        if (level <= LogLevel.Info) rawInfo(message)
    }

    fun info(throwable: Throwable) {
        if (level <= LogLevel.Info) rawInfo(throwable.message, throwable)
    }

    fun info(message: String? = null, throwable: Throwable? = null) {
        if (level <= LogLevel.Info) rawInfo(message, throwable)
    }

    inline fun info(message: () -> String) {
        if (level <= LogLevel.Info) rawInfo(message())
    }

    // --------------------------------------------------------------------------------
    // Warning
    // --------------------------------------------------------------------------------

    abstract fun rawWarning(message: String? = null, throwable: Throwable? = null)

    fun warning(message: String) {
        if (level <= LogLevel.Warning) rawWarning(message)
    }

    fun warning(throwable: Throwable) {
        if (level <= LogLevel.Warning) rawWarning(throwable.message, throwable)
    }

    fun warning(message: String, throwable: Throwable) {
        if (level <= LogLevel.Warning) rawWarning(message, throwable)
    }

    inline fun warning(throwable: Throwable? = null, message: () -> String) {
        if (level <= LogLevel.Warning) rawWarning(message(), throwable)
    }

    inline fun AdaptiveLogger.warning(message: () -> String) {
        if (level <= LogLevel.Warning) warning(message())
    }

    // --------------------------------------------------------------------------------
    // Error
    // --------------------------------------------------------------------------------

    abstract fun rawError(message: String? = null, throwable: Throwable? = null)

    inline fun error(throwable: Throwable, message: () -> String) {
        error(message(), throwable)
    }

    fun error(throwable: Throwable) {
        if (level <= LogLevel.Error) rawError(throwable.message, throwable)
    }

    fun error(message: String, throwable: Throwable? = null) {
        if (level <= LogLevel.Error) rawError(message, throwable)
    }

    inline fun error(message: () -> String) {
        if (level <= LogLevel.Error) error(message())
    }

    // --------------------------------------------------------------------------------
    // Fatal
    // --------------------------------------------------------------------------------

    abstract fun fatal(message: String, throwable: Throwable? = null): Nothing

    fun enableFine(): AdaptiveLogger {
        level = LogLevel.Fine
        return this
    }

}