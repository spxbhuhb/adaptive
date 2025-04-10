package `fun`.adaptive.cookbook.recipe.ui.button

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.generated.resources.grid_view
import `fun`.adaptive.cookbook.generated.resources.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.button.dangerButton
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun buttonRecipe() : AdaptiveFragment {
    var counter = 0

    column {
        maxSize .. verticalScroll

        text("Counter: $counter")
        flowBox {
            gap { 16.dp }
            button("Hello World", Graphics.mail) .. onClick { counter ++ }
            dangerButton("Hello World", Graphics.grid_view) .. onClick { counter ++ }
        }
    }

    return fragment()
}
