package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.sheet.control.add
import `fun`.adaptive.grove.sheet.control.move
import `fun`.adaptive.grove.sheet.control.select
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.instruction.layout.Frame
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.vmNowMicro

@Adaptive
fun controlLayer(viewModel: SheetViewModel) {

    var moveStart = 0L
    var startPosition = Position.NaP
    var lastPosition = Position.NaP
    val selection = autoItem(viewModel.selection) ?: viewModel.emptySelection
    var controlFrame = selection.containingFrame.toFrame(adapter()).grow(1.0)

    dropTarget {

        onDrop { event ->
            val template = (event.transferData?.data as? LfmDescendant) ?: return@onDrop
            viewModel.add(event.x, event.y, template)
        }

        box {
            maxSize

            onPrimaryDown { event ->
                startPosition = event.position
                lastPosition = startPosition

                moveStart = vmNowMicro()

                if (lastPosition !in controlFrame) {
                    viewModel.select(event.x, event.y, EventModifier.SHIFT in event)
                }
            }

            onMove { event ->
                if (lastPosition === Position.NaP) return@onMove
                val newPosition = event.position

                if (selection.isEmpty()) {
                    controlFrame = Frame(startPosition, newPosition)
                } else {
                    viewModel.move(moveStart, newPosition.left - lastPosition.left, newPosition.top - lastPosition.top)
                }

                lastPosition = newPosition
            }

            onPrimaryUp { event ->
                lastPosition = Position.NaP
                moveStart = 0L

                if (selection.isEmpty()) {
                    controlFrame = Frame.NaF
                    viewModel.select(startPosition.toRaw(adapter()), event.x, event.y, EventModifier.SHIFT in event)
                }
            }

            if (controlFrame != Frame.NaF) {
                box {
                    controlFrame .. borders.outline
                }
            }
        }
    }
}