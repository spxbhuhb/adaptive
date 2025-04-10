package `fun`.adaptive.iot.device.ui.browser

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.model.NamedItemType

class DeviceBrowserConfig(
    val name: String,
    val icon: GraphicsResourceSet,
    val itemType : NamedItemType,
    val filterKey: FragmentKey? = null,
    val headerKey: FragmentKey? = null,
    val itemKey: FragmentKey? = null
) {
    lateinit var controller : DeviceBrowserToolController
}