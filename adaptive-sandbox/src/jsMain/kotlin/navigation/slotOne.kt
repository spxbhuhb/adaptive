/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package navigation

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.ui.common.fragment.slot
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.navClick

val slotOneKey = name("slotOne")

@Adaptive
fun slotOne() {
    slot(slotOneKey) { content1() }
}

@Adaptive
private fun content1() {
    text("this is content 1 (click to change)") .. navClick(slotOneKey) { content2() }
}

@Adaptive
private fun content2() {
    text("this is content 2 (click to change)") .. navClick(slotOneKey) { content1() }
}