package `fun`.adaptive.cookbook.ui.snackbar

import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.noPointerEvents
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.rootBox
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.snackbar.Snack
import `fun`.adaptive.ui.snackbar.activeSnacks
import `fun`.adaptive.ui.snackbar.fail
import `fun`.adaptive.ui.snackbar.info
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snackbarTheme
import `fun`.adaptive.ui.snackbar.snacks
import `fun`.adaptive.ui.snackbar.success
import `fun`.adaptive.ui.snackbar.warning
import `fun`.adaptive.ui.theme.backgrounds

@Adaptive
fun snackbarRecipe() {
//    val metrics = mediaMetrics()
    val snacks = autoList(snacks, Snack.adatWireFormat) { snacks.connectInfo(AutoConnectionType.Direct) } ?: emptyList()
//    val activeSnacks = autoList(activeSnacks, Snack.adatWireFormat) { activeSnacks.connectInfo(AutoConnectionType.Direct) } ?: emptyList()

//    val globalPos = position(
//        metrics.viewHeight.dp - (snackbarTheme.snackHeight + snackbarTheme.snackGap) * activeSnacks.size,
//        metrics.viewWidth.dp - snackbarTheme.snackWidth - 16.dp
//    )

    box {
        column {
            noSelect
            gap { 16.dp }

            row {
                gap { 16.dp }

                button("Success") .. onClick { success("Success snackbar!") }
                button("Info") .. onClick { info("Info snackbar!") }
                button("Warning") .. onClick { warning("Warning snackbar!") }
                button("Fail") .. onClick { fail("Fail snackbar!") }
            }

            snackList(snacks)
        }

//        rootBox {
//            noPointerEvents .. maxSize
//            snackList(activeSnacks) .. noPointerEvents .. globalPos
//        }
    }
}