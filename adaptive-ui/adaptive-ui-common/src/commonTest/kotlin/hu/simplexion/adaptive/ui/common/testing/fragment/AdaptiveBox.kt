/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.layout.AbstractBox
import hu.simplexion.adaptive.ui.common.testing.AdaptiveUITestAdapter
import hu.simplexion.adaptive.ui.common.testing.TestReceiver

@AdaptiveActual("test")
class AdaptiveBox(
    adapter: AdaptiveUITestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractBox<TestReceiver, TestReceiver>(adapter, parent, declarationIndex)