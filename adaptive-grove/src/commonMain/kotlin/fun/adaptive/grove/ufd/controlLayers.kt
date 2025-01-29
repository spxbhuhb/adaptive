package `fun`.adaptive.grove.ufd

import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.vmNowMicro

@Adaptive
fun controlLayers(sheetViewModel: SheetViewModel, context: UfdContext) {

    var moveStart = 0L
    var lastPosition = Position.NaP
    val selection = autoItem(sheetViewModel.selection) ?: SheetViewModel.emptySelection
    val controlFrame = selection.containingFrame()?.grow(1.0)

    dropTarget {

        onDrop { context.addDescendant(it) }

        box {
            maxSize

            onPrimaryDown { event ->
                lastPosition = event.position
                moveStart = vmNowMicro()
                context.select(event.x, event.y)
            }

            onMove { event ->
                if (lastPosition === Position.NaP) return@onMove
                val newPosition = event.position
                context.move(moveStart, newPosition.left - lastPosition.left, newPosition.top - lastPosition.top)
                lastPosition = newPosition
            }

            onPrimaryUp {
                lastPosition = Position.NaP
                moveStart = 0L
            }

            if (controlFrame != null) {
                box {
                    controlFrame .. borders.outline
                }
            }
        }
    }
}