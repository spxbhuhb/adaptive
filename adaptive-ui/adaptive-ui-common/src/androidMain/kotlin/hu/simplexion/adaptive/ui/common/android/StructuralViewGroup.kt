/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import hu.simplexion.adaptive.ui.common.AdaptiveUIContainerFragment

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
class StructuralViewGroup(
    context: Context,
    owner: AdaptiveUIContainerFragment<View, ContainerViewGroup>
) : ContainerViewGroup(context, owner) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // no measureChildren here, that is done by the container view group
        val size = owner.layoutFrame.size
        setMeasuredDimension(resolveSize(size.width.toInt(), widthMeasureSpec), resolveSize(size.height.toInt(), heightMeasureSpec))
    }

}
