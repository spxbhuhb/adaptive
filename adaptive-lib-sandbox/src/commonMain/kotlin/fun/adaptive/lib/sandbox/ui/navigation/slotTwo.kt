/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.navigation

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.slot
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.navClick
import `fun`.adaptive.ui.api.route

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