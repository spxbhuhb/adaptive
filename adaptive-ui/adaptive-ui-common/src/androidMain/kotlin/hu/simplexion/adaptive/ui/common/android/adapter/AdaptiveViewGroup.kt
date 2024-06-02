/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
class AdaptiveViewGroup(
    context: Context,
    val layoutFragment: AdaptiveUIContainerFragment<AdaptiveViewGroup, View>
) : ViewGroup(context, null, 0) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // this is necessary, without this the child does not appear
        // TODO check if we do double measure here
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val size = layoutFragment.layoutFrame.size

        setMeasuredDimension(resolveSize(size.width.toInt(), widthMeasureSpec), resolveSize(size.height.toInt(), heightMeasureSpec))
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val size = layoutFragment.layoutFrameOrNull?.size ?: return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        return LayoutParams(size.width.toInt(), size.height.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (item in layoutFragment.items) {
            (item.adapter as AdaptiveAndroidAdapter).applyLayoutToActual(item)
        }
    }

}
