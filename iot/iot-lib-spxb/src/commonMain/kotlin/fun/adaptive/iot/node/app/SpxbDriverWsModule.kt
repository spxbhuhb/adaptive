package `fun`.adaptive.iot.node.app

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.iot.app.IotModule.Companion.iotModule
import `fun`.adaptive.iot.device.network.AioDriverDef
import `fun`.adaptive.iot.node.ui.admin.wsAppSystemSettings
import `fun`.adaptive.iot.node.ui.admin.wsAppSystemSettingsDef
import `fun`.adaptive.iot_lib_spxb.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.iot_lib_spxb.generated.resources.systemSettings
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem

class SpxbDriverWsModule<WT : Workspace> : SpxbClientModule<WT>() {

    val SYSTEM_SETTINGS_KEY : FragmentKey = "iot:ws:system:settings"
    val SYSTEM_SETTINGS_ITEM by lazy { SingularWsItem(Strings.systemSettings, SYSTEM_SETTINGS_KEY, Graphics.settings) }

    override fun resourceInit() {
        application.stringStores += commonMainStringsStringStore0
    }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) {
        super.frontendAdapterInit(adapter)
        with (adapter.fragmentFactory) {
            add(SYSTEM_SETTINGS_KEY, ::wsAppSystemSettings)
        }
    }

    override fun workspaceInit(workspace: WT, session: Any?) {
        application.iotModule += AioDriverDef(
            driverKey,
            "aio_driver_spxb",
            driverKey,
            networkConfigKey,
            networkConfigKey
        )

        with(workspace) {
            wsAppSystemSettingsDef(this@SpxbDriverWsModule)
        }
    }

}