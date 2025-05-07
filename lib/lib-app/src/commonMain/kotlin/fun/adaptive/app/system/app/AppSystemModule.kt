package `fun`.adaptive.app.system.app

import `fun`.adaptive.app.system.model.SystemSettingsSpec
import `fun`.adaptive.lib_app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class AppSystemModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        + SystemSettingsSpec
    }

    override fun resourceInit() {
        // Register shared resource bundles (e.g., strings)
        application.stringStores += commonMainStringsStringStore0
    }
}