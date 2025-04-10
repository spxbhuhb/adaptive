package `fun`.adaptive.ui.value

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.value.item.AvItem


fun iconFor(item: AvItem<*>): GraphicsResourceSet {

    val byType = iconCache[item.type]
    if (byType != null) return byType

    for (marker in item.markers.keys) {
        val icon = iconCache[marker]
        if (icon != null) {
            return icon
        }
    }

    return Graphics.empty
}