package `fun`.adaptive.cookbook.ui.select

import `fun`.adaptive.cookbook.model.E
import `fun`.adaptive.foundation.Adaptive
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
        singleItem()
        many()
        enumSelect()
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

@Adaptive
fun singleItem() {
    val items = listOf("one")
    var selected: String? = null

    column {
        padding { 16.dp }
        text("Selected: $selected")
        select(selected, items) { selected = it } .. inputPlaceholder { "(no item selected)" }
    }
}

@Adaptive
fun many() {
    val items = (1..50).map { "item - $it" }
    var selected: String? = null

    column {
        padding { 16.dp }
        text("Selected: $selected")
        select(selected, items) { selected = it } .. inputPlaceholder { "(no item selected)" }
    }
}

@Adaptive
fun enumSelect() {
    var selected: E? = null

    column {
        padding { 16.dp }
        text("Selected: $selected")
        select(selected, E.entries) { selected = it } .. inputPlaceholder { "(no item selected)" }
    }
}