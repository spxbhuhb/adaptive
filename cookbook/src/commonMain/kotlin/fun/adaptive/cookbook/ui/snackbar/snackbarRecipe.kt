package `fun`.adaptive.cookbook.ui.snackbar

import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.model.AutoConnectionType
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.mediaMetrics
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.rootBox
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.snackbar.Snack
import `fun`.adaptive.ui.snackbar.activeSnacks
import `fun`.adaptive.ui.snackbar.fail
import `fun`.adaptive.ui.snackbar.info
import `fun`.adaptive.ui.snackbar.snackList
import `fun`.adaptive.ui.snackbar.snacks
import `fun`.adaptive.ui.snackbar.success
import `fun`.adaptive.ui.snackbar.warning

@Adaptive
fun snackbarRecipe() {
    val media = mediaMetrics()
    val snacks = autoList(snacks, Snack.adatWireFormat) { snacks.connectInfo(AutoConnectionType.Direct) } ?: emptyList()
    val activeSnacks = autoList(activeSnacks, Snack.adatWireFormat) { activeSnacks.connectInfo(AutoConnectionType.Direct) } ?: emptyList()

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

        rootBox {
            alignItems.endBottom .. height { 200.dp } .. width { 200.dp }
            snackList(activeSnacks)
        }
    }
}