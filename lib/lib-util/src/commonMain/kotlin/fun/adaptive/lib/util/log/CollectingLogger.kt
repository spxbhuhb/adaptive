package `fun`.adaptive.lib.util.log

import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.exitProcessCommon
import kotlinx.datetime.Clock.System.now

class CollectingLogger(
    val name : String? = null,
    val data : CollectedLogData
) : AdaptiveLogger() {

    override fun rawFine(message: String?, throwable: Throwable?) {
        data += CollectedLogItem(name, LogLevel.Fine, now(), message ?: "", throwable?.stackTraceToString())
    }

    override fun rawInfo(message: String?, throwable: Throwable?) {
        data += CollectedLogItem(name, LogLevel.Info, now(), message ?: "", throwable?.stackTraceToString())
    }

    override fun rawWarning(message: String?, throwable: Throwable?) {
        data += CollectedLogItem(name, LogLevel.Warning, now(), message ?: "", throwable?.stackTraceToString())
    }

    override fun rawError(message: String?, throwable: Throwable?) {
        data += CollectedLogItem(name, LogLevel.Error, now(), message ?: "", throwable?.stackTraceToString())
    }

    override fun fatal(message: String, throwable: Throwable?): Nothing {
        data += CollectedLogItem(name, LogLevel.Fatal, now(), message, throwable?.stackTraceToString())
        exitProcessCommon(3210)
    }

}