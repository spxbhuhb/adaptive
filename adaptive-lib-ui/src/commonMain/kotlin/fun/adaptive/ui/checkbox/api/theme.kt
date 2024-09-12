package `fun`.adaptive.ui.checkbox.api

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors

var checkboxTheme = CheckboxTheme()

class CheckboxTheme {

    var active = instructionsOf(
        size(20.dp, 20.dp),
        cornerRadius(10.dp),
        backgrounds.primary,
        textColors.onPrimary,
    )

    var inactive = instructionsOf(
        size(20.dp, 20.dp),
        cornerRadius(10.dp),
        border(colors.outline, 1.dp)
    )

}