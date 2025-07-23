package `fun`.adaptive.runtime

import kotlinx.browser.window

actual fun getPlatformType(): PlatformType {
    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    if ((window as? JsAny) != null) {

        val np = window.navigator.platform.lowercase()
        val nua = window.navigator.userAgent.lowercase()

        when {
            "mac" in np -> PlatformType.JS_BROWSER_MAC
            "win" in np -> PlatformType.JS_BROWSER_WIN
            "linux" in np -> PlatformType.JS_BROWSER_LINUX
            "mac" in nua -> PlatformType.JS_BROWSER_MAC
            "win" in nua -> PlatformType.JS_BROWSER_WIN
            "linux" in nua -> PlatformType.JS_BROWSER_LINUX
            else -> PlatformType.JS_OTHER
        }.also {
            return it
        }

    } else {
        return PlatformType.JS_OTHER
    }
}