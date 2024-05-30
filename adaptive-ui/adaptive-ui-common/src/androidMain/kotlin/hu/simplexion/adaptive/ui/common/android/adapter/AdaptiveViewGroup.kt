/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
class AdaptiveViewGroup(
    context: Context,
    val layoutFragment: AndroidLayoutFragment
) : ViewGroup(context, null, 0) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // this is necessary, without this the child does not appear
        // TODO check if we do double measure here
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val size = layoutFragment.renderInstructions.layoutFrame.size
        setMeasuredDimension(resolveSize(size.width.toInt(), widthMeasureSpec), resolveSize(size.height.toInt(), heightMeasureSpec))
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val frame = layoutFragment.renderInstructions.layoutFrame
        val size = frame.size

        return LayoutParams(size.width.toInt(), size.height.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (item in layoutFragment.items) {
            item.layout(layoutFragment.renderInstructions.layoutFrame)
        }
    }

}
