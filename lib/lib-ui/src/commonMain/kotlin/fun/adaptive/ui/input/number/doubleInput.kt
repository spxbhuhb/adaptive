package `fun`.adaptive.ui.input.number

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.input.InputContext
import `fun`.adaptive.ui.input.InputTheme

@Adaptive
fun doubleInput(
    value: Double,
    decimals: Int = 2,
    state: InputContext = InputContext(),
    theme: InputTheme = InputTheme.DEFAULT,
    onChange: (Double) -> Unit
): AdaptiveFragment {
    doubleOrNullInput(value, decimals, state, theme) { v ->
        if (v == null) {
            state.invalid = true
        } else {
            onChange(v)
            state.invalid = false
        }
    } .. instructions()
    return fragment()
}
