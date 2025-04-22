package `fun`.adaptive.ui.splitpane

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box

@Adaptive
fun horizontalSplitDivider(
    theme: SplitPaneTheme = SplitPaneTheme.default
) {
    box {
        theme.splitDividerHorizontalOverlay
        box {
            theme.splitDividerHorizontalVisible
        }
    }
}