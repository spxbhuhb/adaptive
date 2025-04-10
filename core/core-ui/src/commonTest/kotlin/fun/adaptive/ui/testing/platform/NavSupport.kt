/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.testing.platform

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.support.navigation.AbstractNavSupport
import `fun`.adaptive.ui.support.navigation.NavData
import `fun`.adaptive.ui.testing.AuiTestAdapter

class NavSupport(
    val adapter: AuiTestAdapter
) : AbstractNavSupport() {


    override var root = NavData(emptyList(), emptyMap(), null)

    override fun segmentChange(owner: AdaptiveFragment, segment: String?) {

    }
}