/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import android.view.View
import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.platform.ContainerViewGroup
import hu.simplexion.adaptive.ui.common.support.layout.AbstractColumn

@AdaptiveActual(common)
class CommonColumn(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<View, ContainerViewGroup>(adapter, parent, declarationIndex, false)