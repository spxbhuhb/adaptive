/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.View
import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.android.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.android.ContainerViewGroup
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.layout.AbstractGrid

@AdaptiveActual(common)
class AdaptiveGrid(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractGrid<View, ContainerViewGroup>(adapter, parent, declarationIndex)