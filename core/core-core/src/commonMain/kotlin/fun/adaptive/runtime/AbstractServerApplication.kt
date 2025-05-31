package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AbstractServerApplication<BW : BackendWorkspace> : AbstractApplication<NoFrontendWorkspace,BW>() {

    abstract val backend : BackendAdapter

    abstract val backendMainKey : FragmentKey

    fun moduleInit() {
        modules.forEach { it.application = this }
    }

    fun wireFormatInit() {
        modules.forEach { it.wireFormatInit(WireFormatRegistry)  }
    }

    fun loadResources() {
        modules.forEach { it.resourceInit() }
    }

    fun backendAdapterInit(adapter: BackendAdapter) {
        modules.forEach { it.backendAdapterInit(adapter) }
    }

    fun backendWorkspaceInit(workspace : BW) {
        modules.forEach { it.backendWorkspaceInit(workspace, null) }
    }

}