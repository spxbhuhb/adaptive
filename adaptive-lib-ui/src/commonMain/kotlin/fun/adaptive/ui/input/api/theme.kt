package `fun`.adaptive.ui.input.api

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.colors

var inputTheme = InputTheme()

class InputTheme {

    val base = instructionsOf(
        cornerRadius(8.dp),
        border(colors.outline, 1.dp),
        height { 44.dp },
        fontSize { 17.sp },
        lightFont,
        padding(left = 16.dp, right = 16.dp)
    )

    val active = base .. textColor(colors.onSurface) .. backgroundColor(colors.surface)

    val disabled = base .. backgroundColor(colors.surfaceVariant) .. textColor(colors.onSurfaceVariant)

}