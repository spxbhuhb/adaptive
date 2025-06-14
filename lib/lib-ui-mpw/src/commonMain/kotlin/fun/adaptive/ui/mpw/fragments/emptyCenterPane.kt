package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.mpw.model.PaneDef

@Adaptive
fun emptyCenterPane(): AdaptiveFragment {
    box {
        maxSize
    }
    return fragment()
}