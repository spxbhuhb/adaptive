package `fun`.adaptive.ui.filter

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text

@Adaptive
fun <T> quickFilter(selected: T, entries: List<T>, labelFun: T.() -> String, onSelect: (it: T) -> Unit) {
    row {
        filterTheme.container

        for (entry in entries) {
            quickFilterItem(entry, entry == selected, labelFun, onSelect)
        }
    }
}

@Adaptive
private fun <T> quickFilterItem(entry: T, selected: Boolean, labelFun: T.() -> String, onSelect: (it: T) -> Unit) {
    //val hover = hover()

    row {
        filterTheme.item(selected, false) .. onClick { onSelect(entry) }

        text(entry.labelFun()) .. filterTheme.label
    }
}