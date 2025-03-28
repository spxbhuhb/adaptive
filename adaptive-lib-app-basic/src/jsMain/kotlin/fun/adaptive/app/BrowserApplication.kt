package `fun`.adaptive.app

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.util.clientId
import `fun`.adaptive.runtime.ApplicationNodeType
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.browser
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BrowserApplication<WT : Any, ADT : UiClientApplicationData> : UiClientApplication<WT, ADT>() {

    override lateinit var transport: ServiceCallTransport
    override lateinit var backend: BackendAdapter
    override lateinit var frontend: AdaptiveAdapter
    override lateinit var workspace: WT

    fun main() {

        CoroutineScope(Dispatchers.Default).launch {

            GlobalRuntimeContext.nodeType = ApplicationNodeType.Client

            initModules()

            initWireFormats()

            clientId()

            loadResources()

            transport = webSocketTransport(window.location.origin)

            backend = backend(transport) { adapter ->
                initBackendAdapter(adapter)

                localContext(this@BrowserApplication) {
                    actualize(backendMainKey)
                }
            }

            // this must be after backend init as backend init starts the transport
            appData.sessionOrNull = getService<AuthSessionApi>(transport).getSession()

            initWorkspace()

            browser(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                backend = backend
            ) { adapter ->

                with(adapter.defaultTextRenderData) {
                    fontName = defaultFontName
                    fontSize = defaultFontSize
                    fontWeight = defaultFontWeight
                }

                initFrontendAdapter(adapter)

                localContext(this@BrowserApplication) {
                    actualize(frontendMainKey, emptyInstructions, this@BrowserApplication)
                }

            }.also {
                frontend = it
            }
        }

    }

}