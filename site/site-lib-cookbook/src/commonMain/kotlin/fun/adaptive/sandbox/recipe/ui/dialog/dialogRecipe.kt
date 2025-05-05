package `fun`.adaptive.sandbox.recipe.ui.dialog

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.cookbook.generated.resources.check
import `fun`.adaptive.cookbook.generated.resources.grid_view
import `fun`.adaptive.cookbook.generated.resources.mail
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.gridCol
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.dialog.buttonDialog
import `fun`.adaptive.ui.dialog.deprecatedDialog
import `fun`.adaptive.ui.dialog.iconDialog
import `fun`.adaptive.ui.input.text.textInput
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.textColors

@Adaptive
fun dialogRecipe(): AdaptiveFragment {

    column {
        gap { 16.dp }

        dialogBasic()

        buttonDialog("Button Dialog", Graphics.mail, "Button Dialog Title") { close ->
            dialogContent(close)
        }

        row {
            gap { 16.dp }
            iconDialog(Graphics.grid_view, "Icon Dialog Title") { close ->
                dialogContent(close)
            }
            text("(icon dialog)") .. textColors.onSurfaceVariant
        }

        buttonDialog("Independent", Graphics.mail, "Dialog Title") { close ->
            independent(close)
        }

        buttonDialog("Independent Auto", Graphics.mail, "Dialog Title") { close ->
            independentAuto(close)
        }
    }


    return fragment()
}

@Adaptive
private fun dialogBasic() {
    var modalOpen = false

    column {
        button("Basic") .. onClick { modalOpen = true }

        if (modalOpen) {
            deprecatedDialog("Dialog Title") {
                onClose { modalOpen = false }
                dialogContent { modalOpen = false }
            }
        }
    }
}

@Adaptive
private fun dialogContent(close: () -> Unit) {
    grid {
        colTemplate(200.dp, 400.dp) .. rowTemplate(44.dp, 100.dp) .. padding { 32.dp }

        text("Data:") .. alignSelf.startCenter
        textInput("") {  }

        button("Save", Graphics.check) .. gridCol(2) .. alignSelf.endBottom ..
            onClick {
                // save the content
                close()
            }
    }
}