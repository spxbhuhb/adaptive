package `fun`.adaptive.ui.richtext

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.marginBottom
import `fun`.adaptive.ui.instruction.dp

class RichTextTheme {

    val paragraph = instructionsOf(
        marginBottom { 12.dp }
    )

    companion object {
        val DEFAULT = RichTextTheme()
    }
}