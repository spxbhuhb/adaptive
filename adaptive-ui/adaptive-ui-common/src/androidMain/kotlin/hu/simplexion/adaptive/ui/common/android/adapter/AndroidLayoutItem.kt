/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import android.view.View
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.adapter.LayoutItem
import hu.simplexion.adaptive.ui.common.instruction.Frame

class AndroidLayoutItem(
    fragment: AdaptiveUIFragment,
    val receiver: View,
    rowIndex: Int,
    colIndex: Int,
) : LayoutItem(fragment, rowIndex, colIndex) {

    fun layout() {
        val layoutFrame = fragment.renderInstructions.layoutFrame

        check(layoutFrame !== Frame.NaF) { "Missing layout frame in $fragment"}

        receiver.layout(
            layoutFrame.left.toInt(),
            layoutFrame.top.toInt(),
            layoutFrame.left.toInt() + layoutFrame.width.toInt(),
            layoutFrame.top.toInt() + layoutFrame.height.toInt()
        )
    }


}