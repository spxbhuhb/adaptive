/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.testing.adapter

import hu.simplexion.adaptive.ui.common.instruction.Frame

class TestReceiver(
    val testFrame : Frame = Frame.NaF
) {
    val children = mutableListOf<TestReceiver>()

    var testTop = 0f
    var testLeft = 0f
    var testWidth = 0f
    var testHeight = 0f

}