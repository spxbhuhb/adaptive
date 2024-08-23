/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.resource.FileResource
import `fun`.adaptive.ui.common.fragment.structural.CommonSlot
import `fun`.adaptive.ui.common.render.event
import `fun`.adaptive.ui.common.render.text

// --------------------------------------------------------------------
// NavClick
// --------------------------------------------------------------------

fun navClick(
    slotName: Name = Name.ANONYMOUS,
    @DetachName segment: String? = null,
    @AdaptiveDetach detachFun: (handler: DetachHandler) -> Unit
) = NavClick(slotName, segment, detachFun)

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
        val slot = root.firstOrNull { it is CommonSlot && it.name == slotName } ?: return

        slot as CommonSlot
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

// --------------------------------------------------------------------
// Route
// --------------------------------------------------------------------

/**
 * Defines a route for `slot` fragments. See the navigation
 * tutorial for details.
 */
fun route(
    @DetachName segment: String? = null,
    @AdaptiveDetach detachFun: (handler: DetachHandler) -> Unit
) = Route(segment, detachFun)

/**
 * Defines a route for `slot` fragments. See the navigation
 * tutorial for details.
 */
@Adat
class Route(
    @DetachName val segment: String?,
    @AdaptiveDetach val detachFun: (detachFun: DetachHandler) -> Unit
) : AdaptiveInstruction, DetachHandler, AdatClass<Route> {

    var slot: CommonSlot? = null

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        slot?.setContent(origin, detachIndex, segment)
    }

}

// --------------------------------------------------------------------
// HistorySize
// --------------------------------------------------------------------

/**
 * Sets history size for `slot` fragments. See the navigation
 * tutorial for details.
 */
inline fun historySize(sizeFun: () -> Int) = HistorySize(sizeFun())

/**
 * Sets history size route for `slot` fragments. See the navigation
 * tutorial for details.
 */
data class HistorySize(val size: Int) : AdaptiveInstruction

// --------------------------------------------------------------------
// ExternalLink
// --------------------------------------------------------------------

fun externalLink(res: FileResource) = ExternalLink(res.uri)

fun externalLink(href: String) = ExternalLink(href)

data class ExternalLink(val href: String) : AdaptiveInstruction {

    fun openLink(event: UIEvent) {
        event.fragment.uiAdapter.openExternalLink(href)
    }

    override fun apply(subject: Any) {
        event(subject) { it.onClick = OnClick(this::openLink) }
    }

}