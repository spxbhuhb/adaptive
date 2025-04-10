package `fun`.adaptive.cookbook.app

import `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.cookbook.support.E
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

open class CookbookModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        this[E.V1.typeSignature().trimSignature()] = EnumWireFormat(E.entries)
    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

}