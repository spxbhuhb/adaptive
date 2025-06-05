package `fun`.adaptive.app.app

import `fun`.adaptive.lib_app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule

open class AppMainModule<FW : AbstractWorkspace, BW : AbstractWorkspace> : AppModule<FW, BW>() {

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

}