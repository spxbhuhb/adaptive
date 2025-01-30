package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.control.add
import `fun`.adaptive.grove.sheet.control.move
import `fun`.adaptive.grove.sheet.control.select
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.vmNowMicro

@Adaptive
fun controlLayer(viewModel: SheetViewModel) {

    var moveStart = 0L
    var lastPosition = Position.NaP
    val selection = autoItem(viewModel.selection) ?: viewModel.emptySelection
    val controlFrame = selection.containingFrame?.grow(1.0)

    dropTarget {

        onDrop { event ->
            val template = (event.transferData?.data as? LfmDescendant) ?: return@onDrop
            viewModel.add(event.x, event.y, template)
        }

        box {
            maxSize

            onPrimaryDown { event ->
                lastPosition = event.position
                moveStart = vmNowMicro()
                viewModel.select(event.x, event.y)
            }

            onMove { event ->
                if (lastPosition === Position.NaP) return@onMove
                val newPosition = event.position

                viewModel.move(moveStart, newPosition.left - lastPosition.left, newPosition.top - lastPosition.top)

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