/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment.FoundationSlot
import hu.simplexion.adaptive.foundation.instruction.AdaptiveDetach
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.DetachHandler
import hu.simplexion.adaptive.foundation.query.firstOrNull
import hu.simplexion.adaptive.foundation.query.single
import hu.simplexion.adaptive.resource.FileResource
import hu.simplexion.adaptive.ui.common.render.event

// --------------------------------------------------------------------
// Replace
// --------------------------------------------------------------------

fun replace(
    slotName: String? = null,
    @AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit
) = Replace(slotName, slotEntry)

class Replace(
    val slotName: String? = null,
    @AdaptiveDetach val slotEntry: (handler: DetachHandler) -> Unit
) : DetachHandler, AdaptiveInstruction {

    override fun execute() {
        slotEntry(this)
    }

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        if (slotName == null) {
            origin.single<FoundationSlot>(true).setContent(origin, detachIndex)
        } else {
            // FIXME expensive slot search, should create a slot map in the adapter perhaps
            val root = origin.adapter.rootFragment
            val slot = root.firstOrNull(deep = true) { it is FoundationSlot && it.name == slotName } ?: return

            slot as FoundationSlot
            slot.setContent(origin, detachIndex)
        }
    }

    override fun toString(): String {
        return "Replace"
    }
}

// --------------------------------------------------------------------
// ExternalLink
// --------------------------------------------------------------------

fun externalLink(res: FileResource) = ExternalLink(res.uri)

fun externalLink(href: String) = ExternalLink(href)

data class ExternalLink(val href: String) : AdaptiveInstruction {

    fun openLink(event: AdaptiveUIEvent) {
        event.fragment.uiAdapter.openExternalLink(href)
    }

    override fun apply(subject: Any) {
        event(subject) { it.onClick = OnClick(this::openLink) }
    }

}