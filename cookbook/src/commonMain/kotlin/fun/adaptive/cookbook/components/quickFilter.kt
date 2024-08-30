package `fun`.adaptive.cookbook.components

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyStore
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
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp

private val filterHeight = height(38.dp)
private val common = padding(8.dp, 16.dp, 8.dp, 16.dp) .. filterHeight .. alignItems.center
private val active = backgroundColor(0x6259CE) .. textColor(0xffffff) .. cornerRadius(10.dp)
private val normal = instructionsOf(textColor(0x0))
private val label = fontSize(14.sp) .. lightFont .. noSelect

@Adat
class Filter(
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

@Adaptive
fun quickFilter() {
    var filters = copyStore { Filter(Options.First) }

    quickFilter(filters.option, Options.entries.map { it.name to it.label }, onSelect = { filters.option.update { it }})
}

@Adaptive
fun <T> quickFilter(selected : T, entries : List<Pair<T,String>>, onSelect: (it : T) -> Unit) {
    box {
        row {
            filterHeight .. alignItems.center .. gap(8.dp) .. border(color(0xF3F3F3), 1.dp) .. cornerRadius(10.dp) .. backgroundColor(0xffffff)
            padding((- 1).dp)

            for (entry in entries) {
                row {
                    common
                    if (selected == entry.first) active else normal
                    onClick { onSelect(entry.first) }

                    text(entry.second) .. label
                }
            }
        }
    }
}