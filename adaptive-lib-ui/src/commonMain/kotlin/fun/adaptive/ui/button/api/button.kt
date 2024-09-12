package `fun`.adaptive.ui.button.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text

@Adaptive
fun button(label: String, icon: DrawableResource, vararg instructions: AdaptiveInstruction): AdaptiveFragment {
    row(*instructions) {
        buttonTheme.container

        svg(icon) .. buttonTheme.icon

        text(label) .. buttonTheme.text
    }

    return fragment()
}