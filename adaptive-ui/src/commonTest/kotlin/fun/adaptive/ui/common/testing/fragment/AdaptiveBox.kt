/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.fragment.layout.AbstractBox
import `fun`.adaptive.ui.common.testing.CommonTestAdapter
import `fun`.adaptive.ui.common.testing.TestReceiver

@AdaptiveActual("test")
class AdaptiveBox(
    adapter: CommonTestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractBox<TestReceiver, TestReceiver>(adapter, parent, declarationIndex)