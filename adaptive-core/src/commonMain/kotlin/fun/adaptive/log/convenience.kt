package `fun`.adaptive.log

inline fun AdaptiveLogger.fine(message: () -> String) {
    if (level == LogLevel.Fine) fine(message())
}

inline fun AdaptiveLogger.info(message: () -> String) {
    if (level <= LogLevel.Info) fine(message())
}

inline fun AdaptiveLogger.warning(message: () -> String) {
    if (level <= LogLevel.Warning) warning(message())
}

inline fun AdaptiveLogger.error(message: () -> String) {
    if (level <= LogLevel.Error) error(message())
}