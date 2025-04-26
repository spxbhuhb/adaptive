package `fun`.adaptive.lib.util.log

import `fun`.adaptive.log.AdaptiveLogger
import `fun`.adaptive.log.LogLevel
import `fun`.adaptive.utility.exitProcessCommon
import kotlinx.datetime.Clock.System.now

class CollectingLogger(
    val name : String? = null,
    val data : CollectedLogData
) : AdaptiveLogger() {

    override fun rawFine(message: String?, exception: Exception?) {
        data += CollectedLogItem(name, LogLevel.Fine, now(), message ?: "", exception?.stackTraceToString())
    }

    override fun rawInfo(message: String?, exception: Exception?) {
        data += CollectedLogItem(name, LogLevel.Info, now(), message ?: "", exception?.stackTraceToString())
    }

    override fun rawWarning(message: String?, exception: Exception?) {
        data += CollectedLogItem(name, LogLevel.Warning, now(), message ?: "", exception?.stackTraceToString())
    }

    override fun rawError(message: String?, exception: Exception?) {
        data += CollectedLogItem(name, LogLevel.Error, now(), message ?: "", exception?.stackTraceToString())
    }

    override fun fatal(message: String, exception: Exception?): Nothing {
        data += CollectedLogItem(name, LogLevel.Fatal, now(), message, exception?.stackTraceToString())
        exitProcessCommon(3210)
    }

}