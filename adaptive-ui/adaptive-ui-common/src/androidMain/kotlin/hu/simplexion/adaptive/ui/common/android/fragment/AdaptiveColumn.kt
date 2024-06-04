/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.View
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.android.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.android.AdaptiveViewGroup
import hu.simplexion.adaptive.ui.common.commonUI
import hu.simplexion.adaptive.ui.common.layout.AbstractColumn

class AdaptiveColumn(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AbstractColumn<AdaptiveViewGroup, View>(adapter, parent, declarationIndex, false) {

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveColumn"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveColumn(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}