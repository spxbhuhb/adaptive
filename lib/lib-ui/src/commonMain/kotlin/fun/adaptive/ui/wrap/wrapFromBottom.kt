package `fun`.adaptive.ui.wrap

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.splitPane
import `fun`.adaptive.ui.fragment.layout.SplitPaneViewBackend
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility

@Adaptive
fun wrapFromBottom(
    wrapperWidth : DPixel,
    wrapper: @Adaptive () -> Unit,
    wrapped: @Adaptive () -> Unit
): AdaptiveFragment {

    splitPane(
        SplitPaneViewBackend(
            SplitVisibility.Both,
            SplitMethod.WrapFirst,
            wrapperWidth.value,
            Orientation.Vertical,
            dividerOverlaySize = 0.dp,
            dividerEffectiveSize = 0.dp
        ),
        wrapped,
        { },
        wrapper,
    ) .. instructions()

    return fragment()
}
