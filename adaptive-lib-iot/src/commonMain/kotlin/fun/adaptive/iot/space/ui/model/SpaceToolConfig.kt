package `fun`.adaptive.iot.space.ui.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

class SpaceToolConfig(
    val name: String,
    val icon: GraphicsResourceSet,
    val filterKey: FragmentKey? = null,
    val headerKey: FragmentKey? = null,
    val itemKey: FragmentKey? = null
)