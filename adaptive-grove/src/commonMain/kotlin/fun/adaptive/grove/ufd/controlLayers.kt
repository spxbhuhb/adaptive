package `fun`.adaptive.grove.ufd

import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.query.firstParentWith
import `fun`.adaptive.foundation.query.firstWith
import `fun`.adaptive.grove.sheet.SheetInner
import `fun`.adaptive.grove.sheet.SheetOuter
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun controlLayers(viewModel: UfdViewModel) {

    var lastPosition: Position? = null
    val selection = autoItem(viewModel.selection) ?: viewModel.emptySelection
    val controlFrame = selection.containingFrame()?.grow(1.0)

    dropTarget {

        onDrop { viewModel.addDescendant(it) }

        box {
            maxSize

            onPrimaryDown { event ->
                lastPosition = event.position
                viewModel.select(fragment().firstParentWith<SheetOuter>().firstWith<SheetInner>(), event.x, event.y)
//                selection.update(selectionOf(event))
//                target.update(emptySelection())
            }

            onMove { event ->
                if (lastPosition == null) return@onMove
                val newPosition = event.position
//                selection.move(lastPosition !!, newPosition)
                lastPosition = newPosition
//                target.update(selectionOf(event))
            }

            onPrimaryUp {
                lastPosition = null
//                selection.place(target)
//                target.update(emptySelection())
            }

            if (controlFrame != null) {
                box {
                    controlFrame .. borders.outline
                }
            }
        }
    }
}