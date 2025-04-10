package `fun`.adaptive.ui.filter

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text

@Adaptive
fun <T> quickFilter(
    model: QuickFilterModel<T>,
    selectFun: (entry: T) -> Unit
) {
    row {
        model.theme.container

        for (entry in model.entries) {
            quickFilterItem(model, entry) .. onClick { selectFun(entry) }
        }
    }
}

@Adaptive
private fun <T> quickFilterItem(
    model: QuickFilterModel<T>,
    entry: T
): AdaptiveFragment {
    //val hover = hover()

    row(instructions()) {
        model.theme.item(model.selected == entry, false)

        text(model.labelFun(entry)) .. model.theme.label
    }

    return fragment()
}