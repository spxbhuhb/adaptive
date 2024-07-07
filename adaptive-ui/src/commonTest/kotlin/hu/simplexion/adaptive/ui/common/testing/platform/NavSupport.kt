/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.testing.platform

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.support.navigation.AbstractNavSupport
import hu.simplexion.adaptive.ui.common.support.navigation.NavData
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter

class NavSupport(
    val adapter: CommonTestAdapter
) : AbstractNavSupport() {


    override var root = NavData(emptyList(), emptyMap(), null)

    override fun segmentChange(owner: AdaptiveFragment, segment: String?) {

    }
}