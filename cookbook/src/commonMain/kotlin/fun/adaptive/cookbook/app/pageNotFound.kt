package `fun`.adaptive.cookbook.app

import `fun`.adaptive.cookbook.support.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.navigation.NavState

@Adaptive
fun pageNotFound(navState: NavState?) {
    box {
        maxSize .. alignItems.center

        grid {
            width{ 400.dp }
            rowTemplate(32.dp, 96.dp, 1.fr)
            alignItems.center .. gap { 32.dp }
            paddingTop { 32.dp } .. verticalScroll

            title("404 - Page Not Found")

            flowText(
                """
                    Well, this is embarrassing. The page you are looking for does not exist.
                    Let us know and we'll try our best to fix it. Make sure to send the 
                    screenshot of this message.
                """.trimIndent()
            )

            flowText(navState?.toString() ?: "There is no additional technical information.")
        }
    }
}