/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.TextView
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import kotlin.math.max

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

        val frame = layoutFragment.frame ?: DEFAULT_RECT
        setMeasuredDimension(frame.width.toInt(), frame.height.toInt())
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val frame = layoutFragment.frame ?: DEFAULT_RECT
        return LayoutParams(frame.width.toInt(), frame.height.toInt(), frame.x.toInt(), frame.y.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (item in layoutFragment.items) {
            val rect = item.fragment.frame ?: DEFAULT_RECT
            item.receiver.layout(
                rect.x.toInt(),
                rect.y.toInt(),
                rect.x.toInt() + rect.width.toInt(),
                rect.y.toInt() + rect.height.toInt()
            )
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

    companion object {
        val DEFAULT_RECT = BoundingRect(0f, 0f, 200f, 200f)
    }

}
