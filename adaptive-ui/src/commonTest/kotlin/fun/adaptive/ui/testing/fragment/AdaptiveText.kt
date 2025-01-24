/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.fragment.AbstractText
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.TestReceiver

@AdaptiveActual("test")
class AdaptiveText(
    adapter: AuiTestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractText<TestReceiver>(adapter, parent, index) {

    override val receiver = TestReceiver()

    override fun measureText(content: String) {
        renderData.innerWidth = content.length.toDouble() * 20.0
        renderData.innerHeight = 20.0
    }

    override fun setReceiverContent(content: String) {
        receiver.textContent = content
    }

    override fun getReceiverContent(): String? {
        return receiver.textContent
    }
}