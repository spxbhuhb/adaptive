package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.overflow
import `fun`.adaptive.ui.api.scroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Size

@Adaptive
fun sheet(controller: SheetViewController) {

    val sheetSize = Size(3000.dp, 3000.dp)

    box {
        maxSize .. scroll .. overflow.hidden

        drawingLayer(controller) { sheetSize }

        box {
            sheetSize
            controlLayer(controller)
        }
    }

}