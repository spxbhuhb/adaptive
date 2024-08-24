/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.fragment.structural.AuiSlot
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.render.event
import `fun`.adaptive.ui.render.text

@Adat
class NavClick(
    val slotName: Name,
    @DetachName val segment: String?,
    @AdaptiveDetach val detachFun: (handler: DetachHandler) -> Unit
) : DetachHandler, AdaptiveInstruction {

    override fun execute() {
        detachFun(this)
    }

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        // FIXME expensive slot search, should create a slot map in the adapter perhaps
        val root = origin.adapter.rootFragment
        val slot = root.firstOrNull { it is AuiSlot && it.name == slotName } ?: return

        slot as AuiSlot
        slot.setContent(origin, detachIndex, segment)
    }

    override fun apply(subject: Any) {
        event(subject) {
            it.onClick = OnClick { execute() }
        }
        text(subject) {
            it.noSelect = true
        }
    }
}
