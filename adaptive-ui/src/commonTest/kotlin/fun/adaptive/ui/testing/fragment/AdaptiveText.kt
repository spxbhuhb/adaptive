/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.TestReceiver

@AdaptiveActual("test")
open class AdaptiveText(
    adapter: AuiTestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<TestReceiver>(adapter, parent, index, 1, 2) {

    override val receiver = TestReceiver()

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun auiPatchInternal() {
        if (haveToPatch(dirtyMask, 1)) {
            renderData.innerWidth = content.length.toDouble() * 20.0
            renderData.innerHeight = 20.0
        }
    }

}