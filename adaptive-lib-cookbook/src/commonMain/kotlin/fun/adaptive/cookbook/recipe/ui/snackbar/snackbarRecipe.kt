package `fun`.adaptive.cookbook.recipe.ui.snackbar

import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.failNotification
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snackStore
import `fun`.adaptive.ui.snackbar.successNotification
import `fun`.adaptive.ui.snackbar.warningNotification

@Adaptive
fun snackbarRecipe(): AdaptiveFragment {
//    val metrics = mediaMetrics()
    val snacks = autoCollection(snackStore) ?: emptyList()
//    val activeSnacks = autoList(activeSnacks, Snack.adatWireFormat) { activeSnacks.connectInfo(AutoConnectionType.Direct) } ?: emptyList()

//    val globalPos = position(
//        metrics.viewHeight.dp - (snackbarTheme.snackHeight + snackbarTheme.snackGap) * activeSnacks.size,
//        metrics.viewWidth.dp - snackbarTheme.snackWidth - 16.dp
//    )

    box {
        maxSize .. verticalScroll

        column {
            noSelect
            gap { 16.dp }

            row {
                gap { 16.dp }

                button("Success") .. onClick { successNotification("Success snackbar!") }
                button("Info") .. onClick { infoNotification("Info snackbar!") }
                button("Warning") .. onClick { warningNotification("Warning snackbar!") }
                button("Fail") .. onClick { failNotification("Fail snackbar!") }
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