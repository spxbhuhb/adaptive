package `fun`.adaptive.app

import `fun`.adaptive.app.platform.BrowserHistoryStateListener
import `fun`.adaptive.auth.api.AuthRoleApi
import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.observableOf
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.transport.LocalServiceCallTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BrowserApplication<WT : FrontendWorkspace> : ClientApplication<WT, BackendWorkspace>() {

    var wireFormatProvider : WireFormatProvider = Proto

    override lateinit var transport: ServiceCallTransport
    override lateinit var backend: BackendAdapter
    override lateinit var frontend: AdaptiveAdapter

    override val backendWorkspace = BackendWorkspace(this)
    override lateinit var frontendWorkspace: WT

    /**
     * Current navigation state of the application
     *
     * - observable of `NavState`
     * - initialized from `window.location.href`
     * - changed by:
     *   - browser `popstate` event
     *   - [push](function://BrowserHistoryStateListener)
     */
    override val navState = observableOf { NavState.parse(window.location.href) }

    val historyStateListener = BrowserHistoryStateListener(this)

    open fun buildFrontendWorkspace() = Unit

    fun main() {

        CoroutineScope(Dispatchers.Default).launch {

            moduleInit()

            wireFormatInit()

            transport = webSocketTransport(window.location.origin, wireFormatProvider).also { it.start() }

            genericSessionOrNull = getService<AuthSessionApi>(transport).getSession()
            if (genericSessionOrNull != null) {
                knownRoles = getService<AuthRoleApi>(transport).all()
            }

            backend = backend(transport) { adapter ->
                backendAdapterInit(adapter)

                localContext(this@BrowserApplication) {
                    actualize(backendMainKey, null)
                }
            }

            loadResources()

            buildFrontendWorkspace()

            browser(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                backend = backend
            ) { adapter ->

                adapter.application = this@BrowserApplication

                with(adapter.defaultTextRenderData) {
                    fontName = defaultFontName
                    fontSize = defaultFontSize
                    fontWeight = defaultFontWeight
                }

                frontendAdapterInit(adapter)

                localContext(this@BrowserApplication) {
                    actualize(frontendMainKey, null, emptyInstructions, this@BrowserApplication)
                }

            }.also {
                frontend = it
            }
        }

    }

    override fun onSignOut() {
        window.location.reload()
    }

    override fun onSignIn() {
        window.location.reload()
    }
}