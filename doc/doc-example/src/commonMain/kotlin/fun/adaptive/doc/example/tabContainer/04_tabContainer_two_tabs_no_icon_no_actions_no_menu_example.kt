package `fun`.adaptive.doc.example.tabContainer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.utility.UUID

/**
 * # Two tabs, no icon, no actions, no menu
 *
 * A simple [TabContainer] with two [TabPane]s and tooltips. No icons, actions, or menus.
 */
@Adaptive
fun tabTwoTabsNoIconNoActionsNoMenuExample(): AdaptiveFragment {
    val model = TabContainer(
        listOf(
            TabPane(
                UUID(),
                "lib:todo",
                "Pane 1",
                tooltip = "Pane 1 ToolTip"
            ),
            TabPane(
                UUID(),
                "lib:todo",
                "Pane 2",
                tooltip = "Pane 2 ToolTip"
            )
        )
    )
    renderTab(model)
    return fragment()
}
