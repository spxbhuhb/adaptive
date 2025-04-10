package `fun`.adaptive.grove.sheet

import `fun`.adaptive.grove.sheet.model.HandleInfo

object Handles {
    val LEFT_TOP = HandleInfo("left-top", xActive = true, xReverse = true, yActive = true, yReverse = true)
    val LEFT_CENTER = HandleInfo("left-center", xActive = true, xReverse = true, yActive = false, yReverse = false)
    val LEFT_BOTTOM = HandleInfo("left-bottom", xActive = true, xReverse = true, yActive = true, yReverse = false)
    val TOP_CENTER = HandleInfo("top-center", xActive = false, xReverse = false, yActive = true, yReverse = true)
    val RIGHT_TOP = HandleInfo("right-top", xActive = true, xReverse = false, yActive = true, yReverse = true)
    val RIGHT_CENTER = HandleInfo("right-center", xActive = true, xReverse = false, yActive = false, yReverse = false)
    val RIGHT_BOTTOM = HandleInfo("right-bottom", xActive = true, xReverse = false, yActive = true, yReverse = false)
    val BOTTOM_CENTER = HandleInfo("bottom-center", xActive = false, xReverse = false, yActive = true, yReverse = false)
    val LEFT_BORDER = HandleInfo("left-border", xActive = true, xReverse = true, yActive = false, yReverse = false)
    val TOP_BORDER = HandleInfo("top-border", xActive = false, xReverse = false, yActive = true, yReverse = true)
    val RIGHT_BORDER = HandleInfo("right-border", xActive = true, xReverse = false, yActive = false, yReverse = false)
    val BOTTOM_BORDER = HandleInfo("bottom-border", xActive = false, xReverse = false, yActive = true, yReverse = false)
}