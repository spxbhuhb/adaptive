package `fun`.adaptive.ui.table.renderer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.*
import `fun`.adaptive.ui.table.TableCellDef
import kotlin.time.Instant

@Adaptive
fun <ITEM> tableCellTimeAgo10(cellDef : TableCellDef<ITEM, Instant?>, item : ITEM) : AdaptiveFragment {
    val now = observe { (adapter() as AbstractAuiAdapter<*,*>).tick10 }.now
    text(cellDef.getFun(item)?.let { timeAgoString(it, now) } ?: "") .. cellDef.instructions(item)
    return fragment()
}