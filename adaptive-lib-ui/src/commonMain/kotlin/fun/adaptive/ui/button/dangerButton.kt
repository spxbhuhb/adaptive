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
fun dangerButton(
    label: String,
    icon: GraphicsResourceSet? = null,
    theme: ButtonTheme = ButtonTheme.DANGER,
    vararg instructions: AdaptiveInstruction
): AdaptiveFragment {
    button(label, icon, theme, instructions())
    return fragment()
}