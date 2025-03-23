package `fun`.adaptive.iot.common

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.value.item.AvStatus

@Adaptive
fun status(status: AvStatus) {
    val border = when {
        status.isOk -> border(colors.success, 2.dp)
        else -> borders.fail
    }
    box {
        alignItems.center .. border
        text("Status")
    }
}