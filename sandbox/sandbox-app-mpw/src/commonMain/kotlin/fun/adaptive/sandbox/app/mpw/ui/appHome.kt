package `fun`.adaptive.sandbox.app.mpw.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.misc.todo

@Adaptive
fun appHome(): AdaptiveFragment {

    column {
        maxSize
        todo()
    }

    return fragment()
}