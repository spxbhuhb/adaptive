/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.testing.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.fragment.layout.AbstractBox
import `fun`.adaptive.ui.testing.AuiTestAdapter
import `fun`.adaptive.ui.testing.TestReceiver

@AdaptiveActual("test")
class AdaptiveBox(
    adapter: AuiTestAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractBox<TestReceiver, TestReceiver>(adapter, parent, declarationIndex)