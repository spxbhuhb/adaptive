package `fun`.adaptive.grove

import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmExternalStateVariable
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.grove.hydration.lfm.LfmInternalStateVariable
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.wireformat.WireFormatRegistry

fun groveRuntimeCommon() {
    val r = WireFormatRegistry

    r += LfmConst
    r += LfmDescendant
    r += LfmExternalStateVariable
    r += LfmFragment
    r += LfmInternalStateVariable
    r += LfmMapping
}