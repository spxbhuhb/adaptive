package `fun`.adaptive.doc.example.tabContainer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.utility.UUID

/**
 * # One tab, no icon, no actions, no menu
 *
 * Creates a [TabContainer] with a single [TabPane] without icon, actions or any menu.
 * The tab shows a simple title and tooltip.
 */
@Adaptive
fun tabOneTabNoIconNoActionsNoMenuExample(): AdaptiveFragment {
    val model = TabContainer(
        listOf(
            TabPane(
                UUID(),
                "lib:todo",
                "Pane 1",
                tooltip = "Pane 1 ToolTip"
            )
        )
    )
    renderTab(model)
    return fragment()
}
