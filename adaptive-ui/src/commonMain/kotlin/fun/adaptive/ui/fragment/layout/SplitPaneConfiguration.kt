package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility

@Adat
class SplitPaneConfiguration(
    val visibility : SplitVisibility,
    val method : SplitMethod,
    val split : Double,
    val orientation : Orientation,
    val dividerSize : DPixel
)