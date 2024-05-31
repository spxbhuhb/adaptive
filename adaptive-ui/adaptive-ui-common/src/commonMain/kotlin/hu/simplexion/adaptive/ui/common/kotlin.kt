/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.checkIfInstance

inline fun <reified RT> AdaptiveFragment.checkReceiver() : RT =
    this.checkIfInstance<AdaptiveUIFragment<*>>().receiver.checkIfInstance<RT>()

inline fun <reified RT> AdaptiveFragment.alsoIfReceiver(block : (it : RT) -> Unit) =
    this.checkIfInstance<AdaptiveUIFragment<*>>().receiver?.alsoIfInstance<RT>(block)
