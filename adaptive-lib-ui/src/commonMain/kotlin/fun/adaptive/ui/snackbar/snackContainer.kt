package `fun`.adaptive.ui.snackbar

import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.noPointerEvents
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun snackContainer() {
    val metrics = mediaMetrics()

    val activeSnacks = autoCollection(activeSnackStore) ?: emptyList()

    val snackbarPosition = position(
        metrics.viewHeight.dp - (snackbarTheme.snackHeight + snackbarTheme.snackGap) * activeSnacks.size,
        metrics.viewWidth.dp - snackbarTheme.snackWidth - 16.dp
    )

    box {
        noPointerEvents .. maxSize .. zIndex { 3000 }
        snackList(activeSnacks) .. noPointerEvents .. snackbarPosition
    }
}