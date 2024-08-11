/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.navigation

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.common.fragment.slot
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.navClick

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