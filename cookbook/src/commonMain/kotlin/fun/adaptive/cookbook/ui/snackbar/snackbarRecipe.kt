package `fun`.adaptive.cookbook.ui.snackbar

import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.fail
import `fun`.adaptive.ui.snackbar.info
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snacks
import `fun`.adaptive.ui.snackbar.success
import `fun`.adaptive.ui.snackbar.warning

@Adaptive
fun snackbarRecipe(): AdaptiveFragment {
//    val metrics = mediaMetrics()
    val snacks = autoCollection(snacks) ?: emptyList()
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

    return fragment()
}