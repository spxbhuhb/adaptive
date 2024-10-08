/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractContainer

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
open class ContainerViewGroup(
    context: Context,
    val owner: AbstractContainer<View, ContainerViewGroup>
) : ViewGroup(context, null, 0) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // this is necessary, without this the child does not appear
        // TODO check if we do double measure here
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val data = owner.renderData

        setMeasuredDimension(
            resolveSize(data.finalWidth.toInt(), widthMeasureSpec),
            resolveSize(data.finalHeight.toInt(), heightMeasureSpec)
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        val layout = owner.renderData
        return LayoutParams(layout.finalWidth.toInt(), layout.finalHeight.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val uiAdapter = owner.uiAdapter as AuiAdapter

        for (item in owner.directItems) {
            uiAdapter.applyLayoutToActual(item)
        }

        for (item in owner.structuralItems) {
            uiAdapter.applyLayoutToActual(item)
        }
    }

}
