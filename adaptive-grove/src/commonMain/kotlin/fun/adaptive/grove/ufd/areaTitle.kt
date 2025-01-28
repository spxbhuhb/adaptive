package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun areaTitle(title: String, icon: GraphicsResourceSet? = null) {
    row {
        maxWidth .. borderBottom(colors.outline) .. padding { 4.dp } .. gap { 4.dp }

        if (icon != null) {
            icon(icon)
        }

        text(title)
    }

}