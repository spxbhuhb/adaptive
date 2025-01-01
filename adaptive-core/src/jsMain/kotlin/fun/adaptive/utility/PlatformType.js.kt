package `fun`.adaptive.utility

import kotlinx.browser.window

actual val platformType: PlatformType
    get() = if (window.asDynamic() != null) {
        PlatformType.JsBrowser
    } else {
        PlatformType.JsNode
    }