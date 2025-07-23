package `fun`.adaptive.log

actual fun getPlatformLogger(name: String): AdaptiveLogger {
    return StdoutLogger(name) // FIXME proper logger for WASM
}