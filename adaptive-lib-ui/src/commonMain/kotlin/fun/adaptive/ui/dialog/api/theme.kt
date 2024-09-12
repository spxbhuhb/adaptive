package `fun`.adaptive.ui.dialog.api

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.cornerTopRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textColors

var dialogTheme = DialogTheme()

class DialogTheme {

    val root = instructionsOf(
        maxSize,
        zIndex { 200 },
        backgrounds.overlay,
        alignItems.center
    )

    val mainContainer = instructionsOf(
        backgrounds.surface,
        cornerRadius(16.dp)
    )

    val titleContainer = instructionsOf(
        paddingLeft(32.dp),
        paddingRight(32.dp),
        cornerTopRadius(16.dp),
        backgrounds.primary,
        spaceBetween,
        AlignItems(horizontal = null, vertical = Alignment.Center)
    )

    val titleText = instructionsOf(
        textColors.onPrimary,
        fontSize(24.sp)
    )

    val titleIcon = instructionsOf(
        size(24.dp, 24.dp),
        iconColors.onPrimary
    )

}
