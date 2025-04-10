package `fun`.adaptive.sandbox.recipe.ui.tab

import `fun`.adaptive.cookbook.generated.resources.menu
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.resource.graphics.Graphics
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
    // when these are solved we can just use ::tabHandle (I hope)
    // https://github.com/spxbhuhb/adaptive/issues/123
    // KT-75416 KJS / IC: "IrLinkageError: Constructor can not be called: No constructor found for symbol" on jsBrowserDevelopmentRun restart
    tabContainer(model, {a,b,c,d -> tabHandle(a,b,c,d)}) .. size(400.dp, 100.dp) .. borders.outline
}

val noTabsNoMenu = TabContainer(
    emptyList()
)

val noTabsWithMenu = TabContainer(
    emptyList(),
    "Close tab",
    "Tab Container Menu",
    listOf(MenuItem<Unit>(Graphics.menu, "Menu Item 1", data = Unit))
)

val oneTabNoIconNoActionsNoMenu = TabContainer(
    listOf(
        TabPane(
            UUID(),
            "lib:todo",
            "Pane 1",
            tooltip = "Pane 1 ToolTip"
        )
    )
)

val twoTabsNoIconNoActionsNoMenu = TabContainer(
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