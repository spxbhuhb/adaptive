package `fun`.adaptive.ui.checkbox

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.icon.icon

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
                icon(Graphics.check, theme.icon)
            }
        } else {
            box(theme.inactive) {

            }
        }
    }

    return fragment()
}