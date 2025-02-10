package `fun`.adaptive.grove.sheet

object ControlNames {
    const val LEFT_TOP = "left-top"
    const val LEFT_CENTER = "left-center"
    const val LEFT_BOTTOM = "left-bottom"
    const val TOP_CENTER = "top-center"
    const val RIGHT_TOP = "right-top"
    const val RIGHT_CENTER = "right-center"
    const val RIGHT_BOTTOM = "right-bottom"
    const val BOTTOM_CENTER = "bottom-center"

    const val LEFT_BORDER = "left-border"
    const val TOP_BORDER = "top-border"
    const val RIGHT_BORDER = "right-border"
    const val BOTTOM_BORDER = "bottom-border"

    val topLeftControls = listOf(
        LEFT_TOP, LEFT_CENTER, LEFT_BOTTOM, TOP_CENTER, LEFT_BORDER, TOP_BORDER
    )
}