package `fun`.adaptive.ui.checkbox.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.checkbox.api.theme.CheckboxTheme
import `fun`.adaptive.ui.checkbox.api.theme.checkboxTheme

@Adaptive
fun checkbox(
    value: Boolean,
    theme: CheckboxTheme = checkboxTheme,
    onChange: (Boolean) -> Unit,
): AdaptiveFragment {
    row(instructions()) {

        onClick { onChange(! value) }

        if (value) {
            box(theme.active) {
                svg(Graphics.check, theme.icon)
            }
        } else {
            box(theme.inactive) {

            }
        }
    }

    return fragment()
}