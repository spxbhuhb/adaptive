package `fun`.adaptive.ui.value

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.generated.resources.empty
import `fun`.adaptive.value.AvValue

fun iconFor(item: AvValue<*>): GraphicsResourceSet? {

    for (marker in item.markers) {
        val icon = iconCache[marker]
        if (icon != null) {
            return icon
        }
    }

    return Graphics.empty
}