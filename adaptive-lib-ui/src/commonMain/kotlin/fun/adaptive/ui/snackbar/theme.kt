package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors

val snackbarTheme = SnackbarTheme(
    36.dp,
    16.dp
)

open class SnackbarTheme(
    val snackHeight: DPixel,
    val snackGap: DPixel,
    val snackWidth: DPixel = 280.dp,
) {
    open val container = instructionsOf(
        gap(snackGap)
    )

    open val item = instructionsOf(
        height { snackHeight },
        width { snackWidth },
        padding { 16.dp },
        alignItems.center,
        spaceBetween,
        cornerRadius(8.dp)
    )

    open val success = item + instructionsOf(backgrounds.friendly)
    open val info = item + instructionsOf(backgrounds.overlay)
    open val warning = item + instructionsOf(backgrounds.angry)
    open val fail = item + instructionsOf(backgrounds.angry)

    open fun item(snack: Snack) =
        when (snack.type) {
            SnackType.Success -> success
            SnackType.Info -> info
            SnackType.Warning -> warning
            SnackType.Fail -> fail
        }

    open val successText = instructionsOf(textColors.onSurface)
    open val infoText = instructionsOf(textColors.onSurface)
    open val warningText = instructionsOf(textColors.onSurface)
    open val failText = instructionsOf(textColors.onSurface)

    open fun text(snack: Snack) = when (snack.type) {
        SnackType.Success -> successText
        SnackType.Info -> infoText
        SnackType.Warning -> warningText
        SnackType.Fail -> failText
    }

}