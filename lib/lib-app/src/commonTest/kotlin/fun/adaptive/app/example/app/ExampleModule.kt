package `fun`.adaptive.app.example.app

import `fun`.adaptive.app.example.model.ExampleData
import `fun`.adaptive.app.example.model.ExampleEnum
import `fun`.adaptive.lib_app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry
import kotlin.collections.plusAssign

open class ExampleModule<WT : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<WT, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        // Register Adat classes and enum classes defined in this module
        + ExampleData
        + ExampleEnum
    }

    override fun resourceInit() {
        // Register shared resource bundles (e.g., strings)
        application.stringStores += commonMainStringsStringStore0
    }
}