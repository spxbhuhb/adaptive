package `fun`.adaptive.runtime

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.wireformat.WireFormatRegistry

abstract class AppModule<WT> {

    open fun WireFormatRegistry.init() {

    }

    open suspend fun loadResources() {

    }

    open fun BackendAdapter.init() {

    }

    open fun AdaptiveAdapter.init() {

    }

    open fun WT.init() {

    }

}