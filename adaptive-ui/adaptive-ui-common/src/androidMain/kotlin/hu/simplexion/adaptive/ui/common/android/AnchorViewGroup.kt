/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import hu.simplexion.adaptive.ui.common.AdaptiveUIContainerFragment

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
class AnchorViewGroup(
    context: Context,
    layoutFragment: AdaptiveUIContainerFragment<ContainerViewGroup, View>
) : ContainerViewGroup(context, layoutFragment) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // no measureChildren here, that is done by the container view group
        val size = layoutFragment.layoutFrame.size
        setMeasuredDimension(resolveSize(size.width.toInt(), widthMeasureSpec), resolveSize(size.height.toInt(), heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // this is a no-op, the container group will handle the layout
    }

}
