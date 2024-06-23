/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.support.layout.AbstractContainerFragment
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
class ContainerView(
    val owner: AbstractContainerFragment<UIView, ContainerView>
) : UIView(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    override fun layoutSubviews() {
        val uiAdapter = owner.uiAdapter as CommonAdapter

        for (item in owner.directItems) {
            uiAdapter.applyLayoutToActual(item)
        }

        for (item in owner.structuralItems) {
            uiAdapter.applyLayoutToActual(item)
        }
    }

}
