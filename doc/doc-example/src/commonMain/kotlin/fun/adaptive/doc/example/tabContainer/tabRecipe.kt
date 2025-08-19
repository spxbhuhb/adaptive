package `fun`.adaptive.doc.example.tabContainer

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.tab.TabContainer
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle
import `fun`.adaptive.ui.theme.borders

/**
 * Shared helper to render a TabContainer using tabContainer and a tab handle.
 * Applies a fixed size and an outline border to make the example visually clear.
 */
@Adaptive
fun renderTab(model: TabContainer) {
    // when these are solved we can just use ::tabHandle (I hope)
    // https://github.com/spxbhuhb/adaptive/issues/123
    // KT-75416 KJS / IC: "IrLinkageError: Constructor can not be called: No constructor found for symbol" on jsBrowserDevelopmentRun restart
    tabContainer(model, { a, b, c, d -> tabHandle(a, b, c, d) }) .. size(400.dp, 100.dp) .. borders.outline
}