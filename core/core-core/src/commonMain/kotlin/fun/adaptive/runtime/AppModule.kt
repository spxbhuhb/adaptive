package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AppModule<WT : AbstractWorkspace> {

    lateinit var application : AbstractApplication<WT>

    val workspace
        get() = application.workspace

    open fun wireFormatInit(registry: WireFormatRegistry) {

    }

    open fun resourceInit() {

    }

    open fun backendAdapterInit(adapter : BackendAdapter) {

    }

    open fun frontendAdapterInit(adapter : AdaptiveAdapter) {

    }

    open fun contextInit() {

    }

    open fun workspaceInit(workspace: WT, session: Any?) {

    }

    inline fun fragmentKey(keyFun : () -> FragmentKey) = keyFun()

    // FIXME move content type into multi-pane workspace somehow
    inline fun contentType(keyFun : () -> String) = keyFun()

}