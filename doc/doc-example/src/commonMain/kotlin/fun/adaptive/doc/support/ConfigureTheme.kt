package `fun`.adaptive.doc.support

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.textColors

class ConfigureTheme {

    val titleHeight = 28.dp

    val formContainer = instructionsOf(
        backgrounds.surfaceVariant,
        borders.outline,
        cornerRadius(8.dp)
    )

    val titleContainer = instructionsOf(
        maxSize,
        height(titleHeight),
        cornerRadius { 8.dp },
        alignItems.center
    )

    val titleText = instructionsOf(
        textColors.onSurfaceMedium,
        fontSize { 13.sp },
        semiBoldFont
    )

    companion object {
        val default = ConfigureTheme()
    }
}