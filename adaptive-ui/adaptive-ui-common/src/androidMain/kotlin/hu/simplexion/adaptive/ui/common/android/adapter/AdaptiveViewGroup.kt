/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
class AdaptiveViewGroup(
    context: Context,
    val layoutFragment: AndroidLayoutFragment
) : ViewGroup(context, null, 0) {

    init {
        background = ColorDrawable(Color.BLUE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // this is necessary, without this the child does not appear
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val frame = layoutFragment.renderInstructions.layoutFrame
        setMeasuredDimension(resolveSize(frame.width.toInt(), widthMeasureSpec), resolveSize(frame.height.toInt(), heightMeasureSpec))
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val frame = layoutFragment.renderInstructions.layoutFrame
        return LayoutParams(frame.width.toInt(), frame.height.toInt(), frame.left.toInt(), frame.top.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (item in layoutFragment.items) {
            item.layout()
        }

//        val count = childCount
//
//        for (i in 0 until count) {
//            val child = getChildAt(i)
//            if (child.visibility != GONE) {
//                val lp = child.layoutParams as LayoutParams
//
//                val childLeft: Int = lp.x
//                val childTop: Int = lp.y
//                child.layout(
//                    childLeft, childTop,
//                    childLeft + child.measuredWidth,
//                    childTop + child.measuredHeight
//                )
//            }
//        }
//       val a = AbsoluteLayout(layoutFragment.adapter.context)
    }

    class LayoutParams(width: Int, height: Int, val x : Int, val y : Int) : ViewGroup.LayoutParams(width, height)

}
