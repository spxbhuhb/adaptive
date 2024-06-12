/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.layout.AbstractColumn
import hu.simplexion.adaptive.ui.common.uikit.AdaptiveIosAdapter
import hu.simplexion.adaptive.ui.common.uikit.ContainerView
import platform.UIKit.UIView

@AdaptiveActual(common)
class AdaptiveColumn(
    adapter: AdaptiveIosAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<UIView, ContainerView>(adapter, parent, declarationIndex, false)