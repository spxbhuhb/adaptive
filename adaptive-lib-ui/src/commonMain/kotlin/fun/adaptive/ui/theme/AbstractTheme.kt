package `fun`.adaptive.ui.theme

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp

abstract class AbstractTheme {

    open val inputHeightDp = 28.dp

    open val inputFont = instructionsOf(
        fontSize { 14.sp },
        normalFont
    )

    open val buttonFont = instructionsOf(
        fontSize(13.sp),
        normalFont
    )

    open val inputCornerRadiusDp = 6.dp

    open val inputCornerRadius = cornerRadius { 6.dp }

    open val paneHeaderHeightDp = 42.dp

}