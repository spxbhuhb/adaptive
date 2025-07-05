package `fun`.adaptive.grove.doc.app

import `fun`.adaptive.grove.doc.model.GroveDocExample
import `fun`.adaptive.grove.doc.model.GroveDocExampleGroupSpec
import `fun`.adaptive.grove.doc.model.GroveDocSpec
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class GroveDocModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) {
        with(registry) {
            + GroveDocExampleGroupSpec
            + GroveDocExample
            + GroveDocSpec
        }
    }
}