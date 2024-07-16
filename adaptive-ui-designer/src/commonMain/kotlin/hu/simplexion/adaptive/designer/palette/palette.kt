package hu.simplexion.adaptive.designer.palette

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.text

@Adaptive
fun palette() {
    column {
        text("Box")
        text("Row")
        text("Column")
        text("Grid")
        text("Flow Box")

        text("Text")
        text("Input")
        text("Image")
    }
}