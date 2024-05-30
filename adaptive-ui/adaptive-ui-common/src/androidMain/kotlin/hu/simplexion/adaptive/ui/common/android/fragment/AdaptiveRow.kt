/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.fragment

import android.view.ViewGroup
import android.widget.LinearLayout
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.AdaptiveFragmentCompanion
import hu.simplexion.adaptive.ui.common.android.adapter.AdaptiveAndroidAdapter
import hu.simplexion.adaptive.ui.common.android.adapter.AndroidLayoutFragment
import hu.simplexion.adaptive.ui.common.commonUI

open class AdaptiveRow(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment,
    declarationIndex: Int
) : AndroidLayoutFragment(adapter, parent, declarationIndex, 0, 2) {

    override val viewGroup: ViewGroup
        get() = receiver as ViewGroup

    override fun makeReceiver(): ViewGroup =
        LinearLayout(androidAdapter.context)

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveRow"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveRow(parent.adapter as AdaptiveAndroidAdapter, parent, index)

    }

}