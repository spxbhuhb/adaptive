package `fun`.adaptive.cookbook.ui.dialog

import `fun`.adaptive.cookbook.Res
import `fun`.adaptive.cookbook.check
import `fun`.adaptive.cookbook.grid_view
import `fun`.adaptive.cookbook.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.Independent
import `fun`.adaptive.foundation.producer.poll
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.colSpan
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.gridCol
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.input
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.dialog.api.buttonDialog
import `fun`.adaptive.ui.dialog.api.dialog
import `fun`.adaptive.ui.dialog.api.iconDialog
import `fun`.adaptive.ui.input.api.inputTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.textColors
import kotlinx.datetime.Clock.System.now
import kotlin.math.max
import kotlin.time.Duration.Companion.seconds

@Adaptive
fun dialogRecipe() {

    column {
        gap { 16.dp }

        basic()

        buttonDialog("Button Dialog", Res.drawable.mail, "Button Dialog Title") { close ->
            dialogContent(close)
        }

        row {
            gap { 16.dp }
            iconDialog(Res.drawable.grid_view, "Icon Dialog Title") { close ->
                dialogContent(close)
            }
            text("(icon dialog)") .. textColors.onSurfaceVariant
        }

        buttonDialog("Independent", Res.drawable.mail, "Dialog Title") { close ->
            independent(close)
        }

        buttonDialog("Independent Auto", Res.drawable.mail, "Dialog Title") { close ->
            independentAuto(close)
        }
    }

}

@Adaptive
private fun basic() {
    var modalOpen = false

    column {
        button("Basic") .. onClick { modalOpen = true }

        if (modalOpen) {
            dialog("Dialog Title") {
                onClose { modalOpen = false }
                dialogContent { modalOpen = false }
            }
        }
    }
}

@Adaptive
private fun dialogContent(close: () -> Unit) {
    var data = ""

    grid {
        size(700.dp, 300.dp)
        colTemplate(200.dp, 1.fr) .. rowTemplate(44.dp, 100.dp) .. padding { 32.dp }

        text("Data:") .. alignSelf.startCenter
        input { data } .. inputTheme.active .. maxWidth

        button("Save", Res.drawable.check) .. gridCol(2) .. alignSelf.endBottom ..
            onClick {
                // save the content
                close()
            }
    }
}