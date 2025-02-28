/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.menu.MenuItem
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.TabPane
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.utility.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend {
            auto()
            worker { SnackbarManager() }
        }

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            LibFragmentFactory,
            backend = localBackend
        ) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            column {
                maxSize .. padding { 16.dp } .. gap { 16.dp } .. verticalScroll

                tabExample("No tabs, no menu", noTabsNoMenu)
                tabExample("No tabs with menu", noTabsWithMenu)
                tabExample("One tab, no icon, no actions, no menu", oneTabNoIconNoActionsNoMenu)
                tabExample("Two tabs, no icon, no actions, no menu", twoTabsNoIconNoActionsNoMenu)
            }
        }
    }
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
