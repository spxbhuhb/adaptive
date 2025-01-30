package `fun`.adaptive.grove.sheet.control

import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.Move
import `fun`.adaptive.ui.instruction.DPixel

fun SheetViewModel.move(moveStart : Long, deltaX: DPixel, deltaY: DPixel) {
    this += Move(moveStart, deltaX, deltaY)
}