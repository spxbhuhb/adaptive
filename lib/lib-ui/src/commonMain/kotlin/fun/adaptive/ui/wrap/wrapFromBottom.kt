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
    @Adaptive _KT_74337_wrapper: () -> Unit,
    @Adaptive _KT_74337_wrapped: () -> Unit
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
        _KT_74337_wrapped,
        { },
        _KT_74337_wrapper,
    ) .. instructions()

    return fragment()
}
