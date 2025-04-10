package `fun`.adaptive.ui.button

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text

@Adaptive
fun button(
    label: String,
    icon: GraphicsResourceSet? = null,
    theme : ButtonTheme = ButtonTheme.DEFAULT,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    row(theme.container, instructions()) {
        if (icon != null) svg(icon) .. theme.icon
        text(label) .. theme.text
    }
    return fragment()
}