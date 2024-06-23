/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.support.layout.AbstractContainerFragment

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
open class ContainerViewGroup(
    context: Context,
    val owner: AbstractContainerFragment<View, ContainerViewGroup>
) : ViewGroup(context, null, 0) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // this is necessary, without this the child does not appear
        // TODO check if we do double measure here
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val layout = owner.layoutFrame

        setMeasuredDimension(
            resolveSize(layout.width.toInt(), widthMeasureSpec),
            resolveSize(layout.height.toInt(), heightMeasureSpec)
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val layout = owner.layoutFrameOrNull
        if (layout == null) {
            return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        } else {
            return LayoutParams(layout.width.toInt(), layout.height.toInt())
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val uiAdapter = owner.uiAdapter as CommonAdapter

        for (item in owner.directItems) {
            uiAdapter.applyLayoutToActual(item)
        }

        for (item in owner.structuralItems) {
            uiAdapter.applyLayoutToActual(item)
        }
    }

}
