package `fun`.adaptive.app

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

abstract class UiClientApplication<WT, ADT : UiClientApplicationData> {

    val modules = mutableSetOf<AppModule<WT, UiClientApplication<WT,ADT>>>()

    abstract val transport : ServiceCallTransport

    abstract val backend : BackendAdapter
    abstract val frontend : AdaptiveAdapter

    abstract val appData : ADT
    abstract val workspace : WT

    abstract val backendMainKey : FragmentKey
    abstract val frontendMainKey : FragmentKey

    open val defaultFontName = "Open Sans"
    open val defaultFontSize = 16.sp
    open val defaultFontWeight = 300

    fun initModules() {
        modules.forEach { it.application = this }
    }

    fun initWireFormats() {
        modules.forEach { with(it) { WireFormatRegistry.init() } }
    }

    suspend fun loadResources() {
        coroutineScope {
            modules.map { async { it.loadResources() } }.awaitAll()
        }
    }

    fun initBackendAdapter(adapter: BackendAdapter) {
        modules.forEach { with(it) { adapter.init() } }
    }

    fun initFrontendAdapter(adapter: AdaptiveAdapter) {
        modules.forEach { with(it) { adapter.init() } }
    }

    open fun initWorkspace() {
        modules.forEach { with(it) { workspace.init() } }
    }

    open fun onSignInSuccess() {

    }

    open fun onSignOut() {

    }

}