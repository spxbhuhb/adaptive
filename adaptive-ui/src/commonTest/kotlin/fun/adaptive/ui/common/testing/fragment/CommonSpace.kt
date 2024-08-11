/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.AbstractCommonFragment
import `fun`.adaptive.ui.common.testing.CommonTestAdapter
import `fun`.adaptive.ui.common.testing.TestReceiver

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