package `fun`.adaptive.sandbox.recipe.ui.snackbar

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.*

@Adaptive
fun snackbarRecipe(): AdaptiveFragment {
//    val metrics = mediaMetrics()
    val snacks = valueFrom { snackStore }
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