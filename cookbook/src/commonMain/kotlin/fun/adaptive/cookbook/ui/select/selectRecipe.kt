package `fun`.adaptive.cookbook.ui.select

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.inputPlaceholder
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.toText
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.select.select

@Adaptive
fun selectRecipe() {
    column {
        basicSelect()
        toTextSelect()
    }
}


@Adaptive
fun basicSelect() {
    val items = listOf("one", "two", "three")
    var selected: String? = null

    column {
        padding { 16.dp }
        text("Selected: $selected")
        select(selected, items) { selected = it } .. inputPlaceholder { "(no item selected)" }
    }
}

@Adaptive
fun toTextSelect() {
    val items = listOf(1, 2, 3)
    val names = listOf("one", "two", "three")

    var selected: Int? = null

    column {
        padding { 16.dp }

        text("Selected: $selected")

        select(selected, items) { selected = it } ..
            inputPlaceholder { "(no item selected)" } ..
            toText<Int> { names[it - 1] }
    }

}
