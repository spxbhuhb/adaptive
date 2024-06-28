/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.sandbox.ui.navigation

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.slot
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.navClick
import hu.simplexion.adaptive.ui.common.instruction.route

val slotTwoKey = name("slotTwo")

@Adaptive
fun slotTwo() {
    slot(slotTwoKey) {
        route { content3() }
        route { content4() }

        content3()
    }
}

@Adaptive
private fun content3() {
    text("this is content 3 (click to change)") .. navClick(slotTwoKey) { content4() }
}

@Adaptive
private fun content4() {
    text("this is content 4 (click to change)") .. navClick(slotTwoKey) { content3() }
}