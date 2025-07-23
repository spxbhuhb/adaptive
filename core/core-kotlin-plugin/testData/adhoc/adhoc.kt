package `fun`.adaptive.ui

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.testing.*

@Adaptive
fun adhoc(
    items : List<Int>
): AdaptiveFragment {


    for (item in items) {
        T1(item)
    }

    return fragment()
}

fun box() : String {
    return "OK"
}