/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment.FoundationSlot
import hu.simplexion.adaptive.foundation.instruction.AdaptiveDetach
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.instruction.DetachHandler
import hu.simplexion.adaptive.foundation.query.single
import hu.simplexion.adaptive.resource.FileResource
import hu.simplexion.adaptive.ui.common.render.event

// --------------------------------------------------------------------
// Replace
// --------------------------------------------------------------------

fun replace(
    @AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit
) = Replace(slotEntry)

class Replace(
    @AdaptiveDetach val slotEntry: (handler: DetachHandler) -> Unit
) : DetachHandler, AdaptiveInstruction {

    override fun execute() {
        slotEntry(this)
    }

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        origin.single<FoundationSlot>(true).setContent(origin, detachIndex)
    }

    override fun toString(): String {
        return "Replace"
    }
}

// --------------------------------------------------------------------
// ExternalLink
// --------------------------------------------------------------------

fun externalLink(res : FileResource) = ExternalLink(res.uri)

fun externalLink(href : String) = ExternalLink(href)

data class ExternalLink(val href : String) : AdaptiveInstruction {

    fun openLink(event : AdaptiveUIEvent) {
        event.fragment.uiAdapter.openExternalLink(href)
    }

    override fun apply(subject: Any) {
        event(subject) { it.onClick = OnClick(this::openLink) }
    }

}