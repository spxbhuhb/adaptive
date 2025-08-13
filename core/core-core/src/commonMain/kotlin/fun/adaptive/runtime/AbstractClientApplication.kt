package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

abstract class AbstractClientApplication<FW : AbstractWorkspace, BW : BackendWorkspace> : AbstractApplication<FW,BW>() {

    abstract val transport : ServiceCallTransport

    abstract val backend : BackendAdapter
    abstract val frontend : AdaptiveAdapter

    abstract val backendMainKey : FragmentKey
    abstract val frontendMainKey : FragmentKey

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

    open fun backendWorkspaceInit(workspace : BW) {
        modules.forEach {
            workspace.contexts += it
            it.contextInit()
        }
        modules.forEach { it.backendWorkspaceInit(workspace, genericSessionOrNull) }
    }

    open fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        modules.forEach { it.frontendAdapterInit(adapter) }
    }

    open fun frontendWorkspaceInit(workspace : FW) {
        modules.forEach {
            workspace.contexts += it
            it.contextInit()
        }
        modules.forEach { it.frontendWorkspaceInit(workspace, genericSessionOrNull) }
    }

    open fun hasRole(roleName : String) : Boolean = false

    open fun hasRole(roleUuid : UUID<*>) : Boolean = false

    fun withRole(roleUuid: UUID<*>?, block : () -> Unit) {
        if (roleUuid == null) {
            getLogger("application").warning("using withRole with null role")
            return
        }

        if (hasRole(roleUuid)) {
            block()
        }
    }
}