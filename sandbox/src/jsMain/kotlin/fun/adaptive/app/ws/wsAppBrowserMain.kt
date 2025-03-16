package `fun`.adaptive.app.ws

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.browser
import kotlinx.browser.window

fun wsAppBrowserMain(
    appFragmentFactory : AdaptiveFragmentFactory? = null
) {
    wsAppClientMain(
        webSocketTransport(window.location.origin)
    ) { backend ->

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            LibFragmentFactory,
            backend = backend
        ) { adapter ->

            wsAppAdapterInit(adapter, appFragmentFactory)

            wsAppAdaptiveMain()

        }
    }
}