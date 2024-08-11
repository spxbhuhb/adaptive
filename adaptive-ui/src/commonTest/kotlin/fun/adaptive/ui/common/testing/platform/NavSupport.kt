/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.testing.platform

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.support.navigation.AbstractNavSupport
import `fun`.adaptive.ui.common.support.navigation.NavData
import `fun`.adaptive.ui.common.testing.CommonTestAdapter

class NavSupport(
    val adapter: CommonTestAdapter
) : AbstractNavSupport() {


    override var root = NavData(emptyList(), emptyMap(), null)

    override fun segmentChange(owner: AdaptiveFragment, segment: String?) {

    }
}