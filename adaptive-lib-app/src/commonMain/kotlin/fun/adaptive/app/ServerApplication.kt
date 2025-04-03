package `fun`.adaptive.app

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

abstract class ServerApplication<WT : AbstractWorkspace> : AbstractApplication<WT>() {

    abstract val backend : BackendAdapter

    abstract val backendMainKey : FragmentKey

    fun moduleInit() {
        modules.forEach { it.application = this }
    }

    fun wireFormatInit() {
        modules.forEach { it.wireFormatInit(WireFormatRegistry)  }
    }

    suspend fun resourceLoad() {
        coroutineScope {
            modules.map { async { it.resourceInit() } }.awaitAll()
        }
    }

    fun backendAdapterInit(adapter: BackendAdapter) {
        modules.forEach { it.backendAdapterInit(adapter) }
    }

    fun workspaceInit(workspace : WT) {
        modules.forEach { it.workspaceInit(workspace, null) }
    }

}