package `fun`.adaptive.iot.history.ui

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.model.NamedItemType

@Adat
class HistoryBrowserWsItem(
    override val name: String,
    override val type: NamedItemType,
    val controller: HistoryToolController,
    val items: List<AvValue<*>>
) : NamedItem()