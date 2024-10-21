package `fun`.adaptive.ui.editor.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors
import kotlin.collections.plus

class EditorTheme(
    val height: Int,
    val width: Int,
) {

    var timeWidth = 80.dp
    var dateWidth = 130.dp

    val base = instructionsOf(
        cornerRadius(8.dp),
        height { height.dp },
        width { width.dp },
        fontSize { 17.sp },
        lightFont,
        tabIndex { 0 }
    )

    val enabled = base + instructionsOf(
        border(colors.outline, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.surface)
    )

    val focused = base + instructionsOf(
        border(colors.primary, 2.dp),
        padding(left = 15.dp, right = 15.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.surface),
    )

    val disabled = base + instructionsOf(
        border(colors.outline, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColor(colors.onSurfaceVariant),
        backgroundColor(colors.surfaceVariant)
    )

    val invalidFocused = base + instructionsOf(
        border(colors.onSurfaceAngry, 2.dp),
        padding(left = 15.dp, right = 15.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.angrySurface.opaque(0.05f)),
    )

    val invalidNotFocused = base + instructionsOf(
        border(colors.onSurfaceAngry, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.angrySurface.opaque(0.05f)),
    )

}