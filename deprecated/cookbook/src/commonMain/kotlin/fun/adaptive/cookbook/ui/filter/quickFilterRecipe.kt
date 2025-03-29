package `fun`.adaptive.cookbook.ui.filter

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun quickFilterRecipe() {
    var filters = copyOf { Filter(Options.First) }

    box {
        padding { 16.dp }
        quickFilter(filters.option, Options.entries, { label }, onSelect = { filters.update(filters.copy(option = it)) })
    }
}

@Adat
data class Filter(
    val option: Options
)

enum class Options(
    val label: String
) {
    First("First"),
    Second("Second"),
    Third("Third"),
    Fourth("Fourth")
}