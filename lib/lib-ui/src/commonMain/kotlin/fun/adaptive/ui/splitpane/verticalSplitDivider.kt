package `fun`.adaptive.ui.splitpane

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.box

@Adaptive
fun verticalSplitDivider(
    theme: SplitPaneTheme = SplitPaneTheme.default
) {
    box {
        theme.splitDividerVerticalOverlay
        box {
            theme.splitDividerVerticalVisible
        }
    }
}
