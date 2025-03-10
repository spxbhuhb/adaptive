package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.ui.workspace.model.WsContentPaneGroup

@Adaptive
fun wsContentPaneGroup(
    group: WsContentPaneGroup,
    theme: WorkspaceTheme = workspaceTheme
) {
    // https://github.com/spxbhuhb/adaptive/issues/123
    // KT-75416 KJS / IC: "IrLinkageError: Constructor can not be called: No constructor found for symbol" on jsBrowserDevelopmentRun restart
    tabContainer(group.toTabContainer(), {a,b,c,d -> tabHandle(a,b,c,d)})
}