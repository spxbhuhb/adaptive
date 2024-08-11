/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common.fragment.layout

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.CommonAdapter
import `fun`.adaptive.ui.common.common
import `fun`.adaptive.ui.common.platform.ContainerView
import platform.UIKit.UIView

@AdaptiveActual(common)
class CommonGrid(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<UIView, ContainerView>(adapter, parent, declarationIndex) {
    init {
        receiver.tag = id
    }
}