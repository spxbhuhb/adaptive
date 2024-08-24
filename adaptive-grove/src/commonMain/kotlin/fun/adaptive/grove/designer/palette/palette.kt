package `fun`.adaptive.grove.designer.palette

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text

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