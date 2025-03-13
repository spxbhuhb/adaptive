package `fun`.adaptive.ui.label

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.text


@Adaptive
fun inputLabel(theme: LabelTheme = LabelTheme.DEFAULT, label: () -> String): AdaptiveFragment {
    text(label(), theme.inputLabel, instructions())
    return fragment()
}