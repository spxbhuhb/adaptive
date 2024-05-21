/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIFragment
import org.w3c.dom.Node

abstract class AdaptiveBrowserFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize : Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver : Node

}