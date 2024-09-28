package `fun`.adaptive.cookbook.components

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.copy
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.shared.colors
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hover
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp

@Adaptive
fun quickFilterShort() {
    var filters = copyStore { Filter(Options.First) }

    quickFilter(filters.option, Options.entries, { label }, onSelect = { filters.update(filters.copy(option = it)) })
}

@Adaptive
fun <T> quickFilter(selected: T, entries: List<T>, labelFun: T.() -> String, onSelect: (it: T) -> Unit) {
    box {
        row {
            filterHeight .. alignItems.center .. gap(8.dp) .. border(color(0xF3F3F3), 1.dp) .. cornerRadius(10.dp) .. backgroundColor(0xffffff)

            for (entry in entries) {
                quickFilterItem(entry, entry == selected, labelFun, onSelect)
            }
        }
    }
}

@Adaptive
private fun <T> quickFilterItem(entry: T, selected: Boolean, labelFun: T.() -> String, onSelect: (it: T) -> Unit) {
    val hover = hover()

    row {
        common .. colors(selected, hover) .. onClick { onSelect(entry) }
        if (selected) filterHeight else height { 36.dp }

        text(entry.labelFun()) .. label
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

private val filterHeight = height(38.dp)
private val common = padding(8.dp, 16.dp, 8.dp, 16.dp) .. alignItems.center .. cornerRadius(10.dp)
private val label = fontSize(14.sp) .. lightFont .. noSelect