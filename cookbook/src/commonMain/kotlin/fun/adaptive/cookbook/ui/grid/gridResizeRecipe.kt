package `fun`.adaptive.cookbook.ui.grid

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.shared.blue
import `fun`.adaptive.cookbook.shared.green
import `fun`.adaptive.cookbook.shared.purple
import `fun`.adaptive.cookbook.shared.red
import `fun`.adaptive.cookbook.shared.white
import `fun`.adaptive.cookbook.shared.cyan
import `fun`.adaptive.cookbook.shared.yellow
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onMove
import `fun`.adaptive.ui.api.onPrimaryDown
import `fun`.adaptive.ui.api.onPrimaryUp
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun gridResizeRecipe() {
    var lastPosition: Position? = null
    var contentSize = copyStore { size(400.dp, 200.dp) }

    box {
        size(800.dp, 400.dp) .. borders.outline .. noSelect

        onPrimaryDown { event ->
            lastPosition = event.position
        }

        onMove { event ->
            if (lastPosition == null) return@onMove
            val newPosition = event.position

            val deltaTop = lastPosition?.top?.let { newPosition.top - it } ?: 0.0.dp
            val deltaLeft = lastPosition?.left?.let { newPosition.left - it } ?: 0.0.dp

            contentSize.update(contentSize.copy(height = contentSize.height + deltaTop, width = contentSize.width + deltaLeft))

            lastPosition = newPosition
        }

        onPrimaryUp {
            lastPosition = null
        }

        box {
            contentSize .. borders.outline .. alignSelf.center

            grid {
                maxSize
                colTemplate(40.dp, 1.fr, 1.fr, 1.fr, 40.dp)
                rowTemplate(extend = 44.dp)
                gap(16.dp)

                line(1)
                line(2)
                line(3)
            }
        }

        box {
            maxWidth .. backgrounds.yellow
            text("drag to resize, size: $contentSize, position: $lastPosition") .. noSelect
        }
    }
}

@Adaptive
private fun line(number: Int) {
    row {
        maxSize .. backgrounds.red .. alignItems.center
        text("$number.1") .. textColors.white
    }
    row {
        maxWidth .. backgrounds.green .. alignItems.center
        text("$number.2") .. maxSize
    }
    row {
        maxHeight .. backgrounds.purple .. alignItems.center
        text("$number.3") .. maxHeight .. textColors.white
    }

    text("$number.4") .. maxSize .. alignSelf.startCenter

    row {
        backgrounds.blue
        text("$number.5") .. textColors.white
    }
}
