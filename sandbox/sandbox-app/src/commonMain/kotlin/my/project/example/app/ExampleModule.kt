package my.project.example.app

import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class ExampleModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        // Register Adat classes used across the app

    }

    override fun resourceInit() {
        // Register shared resource bundles (e.g., strings)
    }
}