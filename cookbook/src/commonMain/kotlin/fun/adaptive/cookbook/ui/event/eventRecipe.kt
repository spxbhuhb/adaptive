package `fun`.adaptive.cookbook.ui.event

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.margin
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onDoubleClick
import `fun`.adaptive.ui.api.onMove
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.localTime

@Adaptive
fun eventRecipe() {
    var nonMoveMessages = listOf<String>()
    var moveMessages = listOf<String>()

    grid {
        maxSize .. rowTemplate(200.dp, 1.fr)

        box {
            margin { 16.dp } .. borders.outline .. size(400.dp, 200.dp)

            onClick { nonMoveMessages += "${localTime()} - Click" }
            onDoubleClick { nonMoveMessages += "${localTime()} - Double click" }
            onMove { moveMessages += "${localTime()} - Move + ${it.x}, ${it.y}" }

            text("Click, double click, move")
        }

        grid {
            colTemplate(200.dp, 200.dp) .. maxHeight

            column {
                for (message in nonMoveMessages.reversed()) {
                    text(message)
                }
            }

            column {
                for (message in moveMessages.takeLast(50).reversed()) {
                    text(message)
                }
            }
        }

    }

}

