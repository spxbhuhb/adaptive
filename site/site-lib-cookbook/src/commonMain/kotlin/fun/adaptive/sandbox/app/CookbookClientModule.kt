package `fun`.adaptive.sandbox.app

import `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.sandbox.support.E
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.recipe.ui.mpw.WorkspaceRecipePaneFragmentFactory
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

open class CookbookClientModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW,BW>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        this[E.V1.typeSignature().trimSignature()] = EnumWireFormat(E.entries)
    }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        adapter.fragmentFactory += CookbookFragmentFactory
        adapter.fragmentFactory += WorkspaceRecipePaneFragmentFactory
    }
}