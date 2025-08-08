package `fun`.adaptive.doc.app

import `fun`.adaptive.doc.example.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.doc.support.ExampleEnum
import `fun`.adaptive.doc.support.ExampleValueSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class DocExampleModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW,BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + ExampleEnum
        + ExampleValueSpec
    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

}