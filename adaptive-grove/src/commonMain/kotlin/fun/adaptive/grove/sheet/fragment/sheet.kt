package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize

@Adaptive
fun sheet(viewModel: SheetViewModel) {

    // Boxes in this structure are important because they are used by
    // mechanism to find and fragments. In particular, the box around
    // the drawing layer is used by `select`.

    box {
        maxSize

        drawingLayer(viewModel) { maxSize }

        box {
            maxSize
            controlLayer(viewModel)
        }
    }

}