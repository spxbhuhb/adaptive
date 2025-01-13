package `fun`.adaptive.cookbook.ui.button

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.button.api.dangerButton
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun buttonRecipe() {
    var counter = 0

    var a = false

    column {
        text("Counter: $counter")
        flowBox {
            gap { 16.dp }
            button("Hello World", Graphics.mail) .. onClick { counter ++ }
            dangerButton("Hello World", Graphics.grid_view) .. onClick { counter ++ }
        }
    }

}
