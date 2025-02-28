package `fun`.adaptive.cookbook.recipe.ui.tab

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.utility.UUID

@Adaptive
fun tabRecipe(): AdaptiveFragment {
    column {
        maxSize .. padding { 16.dp } .. gap { 16.dp } .. verticalScroll

        tabExample("No tabs, no menu", noTabsNoMenu)
        tabExample("No tabs with menu", noTabsWithMenu)
        tabExample("One tab, no icon, no actions, no menu", oneTabNoIconNoActionsNoMenu)
        tabExample("Two tabs, no icon, no actions, no menu", twoTabsNoIconNoActionsNoMenu)
    }

    return fragment()
}

@Adaptive
fun tabExample(description: String, model: TabContainer) {
    text(description)
    tabContainer(model, { a, b, c -> tabHandle(model, a, b, c) }) .. size(400.dp, 100.dp) .. borders.outline
}

val noTabsNoMenu = TabContainer(
    emptyList()
)

val noTabsWithMenu = TabContainer(
    emptyList(),
    "Close tab",
    "Tab Container Menu",
    listOf(MenuItem("Menu Item 1", null) { })
)

val oneTabNoIconNoActionsNoMenu = TabContainer(
    listOf(
        TabPane(
            UUID(),
            "lib:todo",
            "Pane 1",
            toolTip = "Pane 1 ToolTip"
        )
    )
)

val twoTabsNoIconNoActionsNoMenu = TabContainer(
    listOf(
        TabPane(
            UUID(),
            "lib:todo",
            "Pane 1",
            toolTip = "Pane 1 ToolTip"
        ),
        TabPane(
            UUID(),
            "lib:todo",
            "Pane 2",
            toolTip = "Pane 2 ToolTip"
        )
    )
)