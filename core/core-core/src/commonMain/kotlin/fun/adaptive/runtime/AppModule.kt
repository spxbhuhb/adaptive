package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AppModule<FW : AbstractWorkspace, BW : AbstractWorkspace> {

    lateinit var application : AbstractApplication<FW,BW>

    val workspace
        get() = application.frontendWorkspace

    open fun wireFormatInit(registry: WireFormatRegistry) {

    }

    open fun resourceInit() {

    }

    open fun contextInit() {

    }
    
    open fun backendAdapterInit(adapter : BackendAdapter) {

    }

    open fun backendWorkspaceInit(workspace: BW, session: Any?) {

    }

    open fun frontendAdapterInit(adapter : AdaptiveAdapter) {

    }

    open fun frontendWorkspaceInit(workspace: FW, session: Any?) {

    }

    inline fun fragmentKey(keyFun : () -> FragmentKey) = keyFun()

    // FIXME move content type into multi-pane workspace somehow
    inline fun contentType(keyFun : () -> String) = keyFun()

}