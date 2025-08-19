package `fun`.adaptive.doc.example.tabContainer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle
import `fun`.adaptive.ui.theme.borders

/**
 * # No tabs, no menu
 *
 * Renders an empty [tabContainer](fragment://) with no tabs and no container menu.
 * This shows the baseline appearance of the tab header with nothing to select.
 */
@Adaptive
fun tabNoTabsNoMenuExample(): AdaptiveFragment {
    val model = TabContainer(emptyList())

    tabContainer(model, ::tabHandle) .. size(400.dp, 100.dp) .. borders.outline

    return fragment()
}
