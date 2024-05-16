/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.basic

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.manualImplementation

@AdaptiveExpect
fun grid(colSpec : TrackSpec, rowSpec : TrackSpec, @Adaptive content : () -> Unit) {
    manualImplementation(colSpec, rowSpec, content)
}

class TrackSpec {

}