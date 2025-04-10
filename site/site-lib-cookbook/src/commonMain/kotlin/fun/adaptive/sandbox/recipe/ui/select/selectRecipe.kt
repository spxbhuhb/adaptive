package `fun`.adaptive.sandbox.recipe.ui.select

import `fun`.adaptive.sandbox.support.E
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.inputPlaceholder
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.toText
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.select.select

@Adaptive
fun selectRecipe(): AdaptiveFragment {
    column {
        maxSize .. verticalScroll

        basicSelect()
        toTextSelect()
        singleItem()
        many()
        enumSelect()
    }

    return fragment()
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