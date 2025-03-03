package `fun`.adaptive.ui.wrap

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.splitPane
import `fun`.adaptive.ui.fragment.layout.SplitPaneConfiguration
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility

@Adaptive
fun wrapFromRight(
    wrapperWidth: DPixel,
    @Adaptive wrapper: () -> Unit,
    @Adaptive wrapped: () -> Unit
): AdaptiveFragment {

    splitPane(
        SplitPaneConfiguration(
            SplitVisibility.Both,
            SplitMethod.WrapFirst,
            wrapperWidth.value,
            Orientation.Horizontal,
            dividerOverlaySize = 0.dp,
            dividerEffectiveSize = 0.dp
        ),
        wrapped,
        { },
        wrapper
    ) .. instructions()

    return fragment()
}
