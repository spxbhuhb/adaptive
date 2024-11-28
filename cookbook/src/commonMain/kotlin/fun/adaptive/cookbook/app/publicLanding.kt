package `fun`.adaptive.cookbook.app

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun publicLanding() {
    box {
        maxSize
        column {
            gap { 12.dp }

            text("Adaptive Cookbook")

            flowText(
                """
                This is the public landing page of the Adaptive Cookbook. 
                Choose a recipe from the side menu or log in with the button at the left bottom.
                """.trimIndent()
            )
        }
    }
}