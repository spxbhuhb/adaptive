package `fun`.adaptive.ui.input.number

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.InputTheme

@Adaptive
fun doubleUnitInput(
    value: Double,
    decimals: Int = 2,
    unit: String,
    state: InputContext = InputContext(),
    theme: InputTheme = InputTheme.DEFAULT,
    onChange: (Double) -> Unit
): AdaptiveFragment {
    row(instructions()) {
        theme.unitContainer

        doubleInput(value, decimals, state, theme) { v -> onChange(v) }
        text(unit) .. theme.unitText
    }
    return fragment()
}
