package `fun`.adaptive.cookbook.form

import `fun`.adaptive.adat.api.hasProblem
import `fun`.adaptive.adat.api.isNotValid
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun formRecipe() {
    column {
        adatFormRecipe()
    }
}

@Adaptive
fun adatFormRecipe() {
    val data = copyStore { FormData() }

    grid {
        rowTemplate(44.dp repeat 2, 60.dp)
        colTemplate(200.dp, 400.dp)
        gap(16.dp) .. alignItems.startCenter

        text("Int:")
        editor { data.int }

        text("String:")
        editor { data.string }

        column {
            text("Valid: ${data.isValid()}")
            text("Has problem: ${data.hasProblem()}")
        }
        button("Save") .. onClick { save(data) }
    }
}

fun save(data: FormData) {
    if (data.isNotValid() || data.hasProblem()) {

    } else {

    }
}
