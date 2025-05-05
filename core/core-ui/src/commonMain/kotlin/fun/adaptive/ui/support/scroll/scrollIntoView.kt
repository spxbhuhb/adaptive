package `fun`.adaptive.ui.support.scroll

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.instruction.layout.Alignment

fun scrollIntoView(declaringFragment : AdaptiveFragment, alignment: Alignment = Alignment.Center) {
    @Suppress("UNCHECKED_CAST")
    val uiAdapter = declaringFragment.adapter as AbstractAuiAdapter<*, *> // TODO change to expectNotNull

    val child = declaringFragment.firstOrNull<AbstractAuiFragment<*>>() ?: return

    uiAdapter.scrollIntoView(child, alignment)
}