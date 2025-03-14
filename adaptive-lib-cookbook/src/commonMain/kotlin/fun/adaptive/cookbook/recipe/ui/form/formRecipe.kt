package `fun`.adaptive.cookbook.recipe.ui.form

import `fun`.adaptive.adat.api.hasProblem
import `fun`.adaptive.adat.api.isNotValid
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun formRecipe(): AdaptiveFragment {
    column {
        maxSize .. verticalScroll

        adatFormRecipe()
    }
    return fragment()
}

@Adaptive
fun adatFormRecipe() {
    val data = copyOf { FormData() }

    grid {
        rowTemplate(44.dp repeat 5, 60.dp)
        colTemplate(200.dp, 400.dp)
        gap(16.dp) .. alignItems.startCenter

        text("Boolean:")
        editor { data.boolean }

        text("Int:")
        editor { data.int }

        text("String:")
        editor { data.string }

        text("Enum:")
        editor { data.enum }

        text("Enum or null:")
        editor { data.enumOrNull }

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
