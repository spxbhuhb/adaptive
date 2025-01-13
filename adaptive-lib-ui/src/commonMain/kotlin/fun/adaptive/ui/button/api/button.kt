package `fun`.adaptive.ui.button.api

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
fun button(label: String, icon: GraphicsResourceSet? = null, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(buttonTheme.container, instructions()) {
        if (icon != null) svg(icon) .. buttonTheme.icon
        text(label) .. buttonTheme.text
    }
    return fragment()
}

@Adaptive
fun dangerButton(label: String, icon: GraphicsResourceSet? = null, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(dangerButtonTheme.container, instructions()) {
        if (icon != null) svg(icon) .. dangerButtonTheme.icon
        text(label) .. dangerButtonTheme.text
    }
    return fragment()
}