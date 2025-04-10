package `fun`.adaptive.ui.dialog

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.iconColors
import `fun`.adaptive.ui.theme.textColors

class DialogTheme {

    val root = instructionsOf(
        maxSize,
        zIndex { 200 },
        backgrounds.overlay,
        alignItems.center
    )

    val mainContainer = instructionsOf(
        width { 700.dp },
        backgrounds.surfaceVariant,
        cornerRadius(8.dp)
    )

    val titleContainer = instructionsOf(
        colTemplate(28.dp, 1.fr),
        rowTemplate(28.dp),
        height { 28.dp },
        cornerRadius { 8.dp }
    )

    val titleText = instructionsOf(
        textColors.onSurfaceVariant,
        fontSize { 13.sp },
        semiBoldFont,
        alignSelf.center
    )

    val titleIcon = instructionsOf(
        size(16.dp, 16.dp),
        iconColors.onSurface,
        svgWidth(16.dp),
        svgHeight(16.dp),
        alignSelf.center,
    )

    companion object {
        val default = DialogTheme()
    }
}
