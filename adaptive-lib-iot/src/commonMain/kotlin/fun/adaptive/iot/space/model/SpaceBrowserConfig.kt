package `fun`.adaptive.iot.space.model

import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

class SpaceBrowserConfig(
    val name: String,
    val icon: GraphicsResourceSet,
    val filterKey: FragmentKey,
    val headerKey: FragmentKey,
    val itemKey: FragmentKey
)