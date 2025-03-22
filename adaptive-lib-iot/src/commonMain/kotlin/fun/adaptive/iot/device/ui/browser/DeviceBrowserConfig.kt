package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.model.WsItemType

class DeviceBrowserConfig(
    val name: String,
    val icon: GraphicsResourceSet,
    val itemType : WsItemType,
    val filterKey: FragmentKey? = null,
    val headerKey: FragmentKey? = null,
    val itemKey: FragmentKey? = null
) {
    lateinit var controller : DeviceBrowserToolController
}