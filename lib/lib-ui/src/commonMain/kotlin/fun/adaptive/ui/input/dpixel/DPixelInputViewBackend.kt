package `fun`.adaptive.ui.input.dpixel

import `fun`.adaptive.ui.input.InputViewBackend
import `fun`.adaptive.ui.instruction.DPixel
import kotlin.properties.Delegates.observable

class DPixelInputViewBackend(
    value: DPixel? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<DPixel, DPixelInputViewBackend>(
    value, label, isSecret
) {

    var decimals by observable(0, ::notify)

}