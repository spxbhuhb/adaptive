package `fun`.adaptive.utility

import kotlinx.browser.window

actual fun exitProcessCommon(status: Int): Nothing {
    // FIXME add exitProcess for node in Js target
    window.location.pathname = "/exitProcess"
    throw RuntimeException("exiting process")
}