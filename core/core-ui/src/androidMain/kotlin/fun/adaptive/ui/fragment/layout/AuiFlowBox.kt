/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.layout

import android.view.View
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.platform.ContainerViewGroup

@AdaptiveActual(aui)
class AuiFlowBox(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractFlowBox<View, ContainerViewGroup>(adapter, parent, declarationIndex)