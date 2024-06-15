/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.support.AbstractGrid
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter
import hu.simplexion.adaptive.ui.common.testing.TestReceiver

@AdaptiveActual("test")
open class AdaptiveGrid(
    adapter: CommonTestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<TestReceiver, TestReceiver>(adapter, parent, declarationIndex)