package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun snackContainer(
    theme : SnackbarTheme = SnackbarTheme.DEFAULT
) {
    val metrics = mediaMetrics()

    val activeSnacks = valueFrom { activeSnackStore }

    val snackbarPosition = position(
        metrics.viewHeight.dp - (theme.snackHeight + theme.snackGap) * activeSnacks.size,
        metrics.viewWidth.dp - theme.snackWidth - 16.dp
    )

    box {
        noPointerEvents .. maxSize .. zIndex { 3000 }
        snackList(activeSnacks) .. noPointerEvents .. snackbarPosition
    }
}