package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AppModule<WT : AbstractWorkspace> {

    lateinit var application : AbstractApplication<WT>

    open fun wireFormatInit(registry: WireFormatRegistry) {

    }

    open fun resourceInit() {

    }

    open fun backendAdapterInit(adapter : BackendAdapter) {

    }

    open fun frontendAdapterInit(adapter : AdaptiveAdapter) {

    }

    open fun workspaceInit(workspace: WT, session: Any?) {

    }

}