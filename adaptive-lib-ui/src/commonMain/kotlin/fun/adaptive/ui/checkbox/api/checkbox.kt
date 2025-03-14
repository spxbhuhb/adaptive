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

@Adaptive
fun checkbox(
    value: Boolean,
    theme: CheckboxTheme = CheckboxTheme.DEFAULT,
    onChange: (Boolean) -> Unit,
): AdaptiveFragment {

    row(theme.container, instructions()) {

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