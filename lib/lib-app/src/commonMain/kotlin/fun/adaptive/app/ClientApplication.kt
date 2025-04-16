package `fun`.adaptive.app

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.ClientWorkspace
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

abstract class ClientApplication<WT : ClientWorkspace> : AbstractApplication<WT>() {

    abstract val transport : ServiceCallTransport

    abstract val backend : BackendAdapter
    abstract val frontend : AdaptiveAdapter

    abstract val backendMainKey : FragmentKey
    abstract val frontendMainKey : FragmentKey

    open val defaultFontName = "Open Sans"
    open val defaultFontSize = 16.sp
    open val defaultFontWeight = 300

    var genericSessionOrNull : Any? = null

    fun moduleInit() {
        modules.forEach { it.application = this }
    }

    fun wireFormatInit() {
        modules.forEach { it.wireFormatInit(WireFormatRegistry) }
    }

    suspend fun loadResources() {
        modules.forEach { it.resourceInit() }
        coroutineScope {
            stringStores.map { async { it.load() } }.awaitAll()
        }
    }

    fun backendAdapterInit(adapter: BackendAdapter) {
        modules.forEach { it.backendAdapterInit(adapter) }
    }

    open fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        modules.forEach { it.frontendAdapterInit(adapter) }
    }

    open fun workspaceInit(workspace : WT) {
        modules.forEach {
            workspace.contexts += it
            it.contextInit()
        }
        modules.forEach { it.workspaceInit(workspace, genericSessionOrNull) }
    }

    open fun onSignIn() {

    }

    open fun onSignOut() {

    }

}