package `fun`.adaptive.utility

import kotlinx.browser.window

actual fun vmNowMicro(): Long {
    // performance.now returns with a double that contains milliseconds
    return (window.performance.now() * 1_000).toLong()
}

actual fun vmNowSecond() : Long {
    return vmNowMicro() / 1_000_000
}

actual fun sleep(millis: Long) {
    throw UnsupportedOperationException("wait is not supported on JS")
}