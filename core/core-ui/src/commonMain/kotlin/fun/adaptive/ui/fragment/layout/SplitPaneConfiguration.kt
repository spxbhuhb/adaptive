package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility

/**
 * @param  split  With fix method the size of the fix pane in DP. With proportional method the ratio
 *                between the first pane and the total size.
 *
 * @param  dividerOverlaySize    The actual size of the divider used for the box. The divider may be actually bigger
 *                               than it seems, to make it easier to grab. In these cases the divider is usually
 *                               mostly transparent. For example, it may look like a simple line with 1 dp width, but
 *                               to make it easy to use it is actually 9 dp wide with a 4 dp transparent margin.
 *
 * @param  dividerEffectiveSize  The size of the divider (width or height, depending on direction) to use for
 *                               layout calculations.
 */
@Adat
data class SplitPaneConfiguration(
    val visibility : SplitVisibility,
    val method : SplitMethod,
    val split : Double,
    val orientation : Orientation,
    val dividerOverlaySize : DPixel = 9.dp,
    val dividerEffectiveSize : DPixel = 1.dp
) {

    fun toggleFirst() =
        when (visibility) {
            SplitVisibility.First -> copy(visibility = SplitVisibility.None)
            SplitVisibility.Second -> copy(visibility = SplitVisibility.Both)
            SplitVisibility.Both -> copy(visibility = SplitVisibility.Second)
            SplitVisibility.None -> copy(visibility = SplitVisibility.First)

        }
}