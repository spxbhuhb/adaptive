/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.ViewGroup
import android.widget.LinearLayout
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveViewGroup
import hu.simplexion.adaptive.ui.common.android.adapter.AndroidLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI

open class AdaptiveColumn(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AndroidLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override fun makeReceiver(): ViewGroup =
        LinearLayout(androidAdapter.context).also {
            it.orientation = LinearLayout.VERTICAL
        }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveColumn"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveColumn(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}