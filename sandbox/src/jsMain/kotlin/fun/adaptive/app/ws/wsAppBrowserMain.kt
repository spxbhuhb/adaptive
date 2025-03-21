package `fun`.adaptive.app.ws

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.chart.chartCommon
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.runtime.ApplicationNodeType
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.value.app.valueClientBackend
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun wsAppBrowserMain(
    appFragmentFactory: AdaptiveFragmentFactory? = null
) {

    GlobalRuntimeContext.nodeType = ApplicationNodeType.Client

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()
        chartCommon()
        iotCommon()

        commonMainStringsStringStore0.load()

        val transport = webSocketTransport(window.location.origin)

        val backend = backend(transport) {
            auto()
            worker { SnackbarManager() }
            valueClientBackend()
        }

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