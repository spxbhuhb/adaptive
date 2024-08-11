/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.platform

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import `fun`.adaptive.ui.common.fragment.layout.AbstractContainer

@SuppressLint("ViewConstructor") // not a general Android view group, you are not supposed to use it in general Android code
class StructuralViewGroup(
    context: Context,
    owner: AbstractContainer<View, ContainerViewGroup>
) : ContainerViewGroup(context, owner) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // no measureChildren here, that is done by the container view group
        val data = owner.renderData

        setMeasuredDimension(
            resolveSize(data.finalWidth.toInt(), widthMeasureSpec),
            resolveSize(data.finalHeight.toInt(), heightMeasureSpec)
        )
    }

}
