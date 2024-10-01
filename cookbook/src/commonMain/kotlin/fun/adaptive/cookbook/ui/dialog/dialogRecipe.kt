package `fun`.adaptive.cookbook.ui.dialog

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.Res
import `fun`.adaptive.ui.builtin.check
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.dialog.api.dialog
import `fun`.adaptive.ui.dialog.api.formDialog
import `fun`.adaptive.ui.form.api.form

@Adaptive
fun dialogRecipe() {
    var modalOpen = false
    var data = false

    column {
        text("Hello World! $data")
        button("Open", Res.drawable.check) .. onClick { modalOpen = true }

        if (modalOpen) {
            dialog("Dialog Title") {
                onClose { modalOpen = false }
                text("hello")
                choice { data = it }
            }
        }
    }
}


@Adaptive
fun choice(f : (Boolean) -> Unit) {
    var a = false
    f(a)
    checkbox { a }
}