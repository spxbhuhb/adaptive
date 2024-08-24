/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.instruction.navigation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.*
import `fun`.adaptive.ui.fragment.structural.AuiSlot

/**
 * Defines a route for `slot` fragments. See the navigation
 * tutorial for details.
 */
@Adat
class Route(
    @DetachName val segment: String?,
    @AdaptiveDetach val detachFun: (detachFun: DetachHandler) -> Unit
) : AdaptiveInstruction, DetachHandler, AdatClass<Route> {

    var slot: AuiSlot? = null

    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        slot?.setContent(origin, detachIndex, segment)
    }

}