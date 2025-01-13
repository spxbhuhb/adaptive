package `fun`.adaptive.cookbook.ui.layout.box

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.cookbook.shared.yellow
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textSmall

@Adaptive
fun addChild(inColumn: Boolean = false) {
    val boxes = autoCollection(boxStore) ?: emptyList()

    box {
        borders.outline
        size(400.dp, 200.dp)
        alignItems.endBottom

        text("Click here to add another box.") .. alignSelf.topCenter .. noSelect .. onClick {
            boxStore += Index(boxes.size)
        }

        boxList(inColumn, boxes)
    }
}


@Adat
private class Index(val index: Int)

private val boxStore = autoCollectionOrigin(listOf<Index>(Index(0), Index(1)))

@Adaptive
private fun boxList(inColumn: Boolean, boxes: Collection<Index>) {
    for (i in boxes) {
        box {
            boxPosition(400.dp - 2.dp, 200.dp - 2.dp, i.index, inColumn) // -2.dp is the border of the container box
            size(40.dp, 20.dp)
            backgrounds.yellow
            border(colors.danger)

            text(i.index) .. alignSelf.center .. noSelect .. textSmall
        }
    }
}

private fun boxPosition(areaWidth: DPixel, areaHeight: DPixel, index: Int, inColumn: Boolean): Position {
    val boxWidth = 40.dp
    val boxHeight = 20.dp
    if (inColumn) {
        val boxesPerColumn = areaHeight / boxHeight
        val row = index % boxesPerColumn.value.toInt()
        val col = index / boxesPerColumn.value.toInt()
        val xPos = areaWidth - boxWidth * (col + 1)
        val yPos = areaHeight - boxHeight * (row + 1)
        return position(yPos, xPos)
    } else {
        val boxesPerRow = areaWidth / boxWidth
        val row = index / boxesPerRow.value.toInt()
        val col = index % boxesPerRow.value.toInt()
        val xPos = areaWidth - boxWidth * (col + 1)
        val yPos = areaHeight - boxHeight * (row + 1)
        return position(yPos, xPos)
    }
}