package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.workspace.model.WsItemType

class SpaceBrowserConfig(
    val name: String,
    val icon: GraphicsResourceSet,
    val itemType : WsItemType,
    val filterKey: FragmentKey? = null,
    val headerKey: FragmentKey? = null,
    val itemKey: FragmentKey? = null
) {
    lateinit var controller : SpaceBrowserToolController
}