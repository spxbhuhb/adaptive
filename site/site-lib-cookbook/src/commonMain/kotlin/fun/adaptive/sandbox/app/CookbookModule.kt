package `fun`.adaptive.sandbox.app

import `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.sandbox.support.ExampleEnum
import `fun`.adaptive.sandbox.support.ExampleValueSpec
import `fun`.adaptive.wireformat.WireFormatRegistry

open class CookbookModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW,BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + ExampleEnum
        + ExampleValueSpec
    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

}