package `fun`.adaptive.ui.theme

import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.instruction.dp

var borders = ThemeBorders()

class ThemeBorders {
    val onSurface = border(colors.onSurface, 1.dp)
    val primary = border(colors.primary, 1.dp)
    val outline = border(colors.outline, 1.dp)
    val friendly = border(colors.onSurfaceFriendly, 1.dp)

    val success = border(colors.success, 1.dp)
    val info = border(colors.info, 1.dp)
    val warning = border(colors.warning, 1.dp)
    val fail = border(colors.fail, 1.dp)

    val onSuccess = border(colors.onSuccessSurface, 1.dp)
    val onInfo = border(colors.onInfoSurface, 1.dp)
    val onWarning = border(colors.onWarningSurface, 1.dp)
    val onFail = border(colors.onFailSurface, 1.dp)
}