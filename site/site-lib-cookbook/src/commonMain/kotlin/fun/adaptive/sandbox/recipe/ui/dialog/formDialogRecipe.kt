package `fun`.adaptive.cookbook.recipe.ui.dialog

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.dialog.formDialog
import `fun`.adaptive.ui.form.form

@Adaptive
fun formDialogRecipe() {
    var modalOpen = false
    val data = copyOf { EditData("Something", "Something else", true) }

    column {
        text("Hello World!")
        button("Open", Graphics.check) .. onClick { modalOpen = true }
        form(data)

        if (modalOpen) {
            formDialog("Edit (TH-001)", data) .. onClose { modalOpen = false }
        }
    }
}

@Adat
private class EditData(
    val displayName: String,
    val group: String,
    val active: Boolean
)
