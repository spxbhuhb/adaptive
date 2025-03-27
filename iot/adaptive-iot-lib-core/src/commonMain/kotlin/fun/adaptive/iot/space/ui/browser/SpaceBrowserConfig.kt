package `fun`.adaptive.iot.space.ui.browser

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.model.NamedItemType

class SpaceBrowserConfig(
    val name: String,
    val icon: GraphicsResourceSet,
    val itemType : NamedItemType,
    val filterKey: FragmentKey? = null,
    val headerKey: FragmentKey,
    val itemKey: FragmentKey
) {
    lateinit var controller : SpaceBrowserToolController
}