package `fun`.adaptive.ui.input

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.text

@Adaptive
fun inputHint(hint : String) : AdaptiveFragment {
    text(hint) .. InputTheme.DEFAULT.hint .. instructions()
    return fragment()
}