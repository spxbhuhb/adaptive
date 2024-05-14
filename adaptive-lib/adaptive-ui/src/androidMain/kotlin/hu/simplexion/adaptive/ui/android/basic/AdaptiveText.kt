/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.basic

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.ui.android.adapter.AdaptiveViewFragment

class AdaptiveText(
    adapter: AdaptiveAdapter<View>,
    parent: AdaptiveFragment<View>,
    index: Int
) : AdaptiveViewFragment(adapter, parent, index, 1, true) {

    override val receiver = TextView(viewAdapter.context)

    private val content: String
        get() = state[0]?.toString() ?: ""

    override fun genPatchInternal() {
        val closureMask = getThisClosureDirtyMask()

        if (haveToPatch(closureMask, 1)) {
            receiver.text = content
            receiver.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

}