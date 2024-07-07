/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.testing.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.testing.CommonTestAdapter
import hu.simplexion.adaptive.ui.common.testing.TestReceiver

@AdaptiveActual("test")
open class CommonSpace(
    adapter: CommonTestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<TestReceiver>(adapter, parent, index, 0, 1) {

    override val receiver = TestReceiver()

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return false
    }

}