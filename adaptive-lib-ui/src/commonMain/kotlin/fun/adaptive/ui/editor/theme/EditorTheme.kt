package `fun`.adaptive.ui.editor.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.select.theme.SelectTheme
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.inputHeightDefault
import `fun`.adaptive.ui.theme.inputWidthDefault

class EditorTheme(
    val height: DPixel = inputHeightDefault,
    val width: DPixel = inputWidthDefault,
) {

    var timeWidth = 80.dp
    var dateWidth = 130.dp

    val base = instructionsOf(
        cornerRadius(8.dp),
        height { height },
        width { width },
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
        backgroundColor(colors.failSurface.opaque(0.05f)),
    )

    val invalidNotFocused = base + instructionsOf(
        border(colors.onSurfaceAngry, 1.dp),
        padding(top = 1.dp, left = 16.dp, bottom = 1.dp, right = 16.dp),
        textColor(colors.onSurface),
        backgroundColor(colors.failSurface.opaque(0.05f)),
    )

    val selectTheme = SelectTheme.DEFAULT

    companion object {
        val DEFAULT = EditorTheme()
    }

}