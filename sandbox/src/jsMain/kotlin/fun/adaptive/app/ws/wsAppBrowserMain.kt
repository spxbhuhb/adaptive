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
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun wsAppBrowserMain(
    appFragmentFactory : AdaptiveFragmentFactory? = null
) {
    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()
        chartCommon()
        iotCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend {
            auto()
            worker { SnackbarManager() }
        }

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            LibFragmentFactory,
            backend = localBackend
        ) { adapter ->

            wsAppUiInit(adapter, appFragmentFactory)

            wsAppUiMain()
        }
    }
}