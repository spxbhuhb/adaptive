/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform

import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.api.UIEvent
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
class ContainerView(
    val owner: AbstractContainer<UIView, ContainerView>
) : UIView(CGRectMake(0.0, 0.0, 0.0, 0.0)) {

    init {
        tag = owner.id
    }

    override fun layoutSubviews() {
        val uiAdapter = owner.uiAdapter as AuiAdapter

        for (item in owner.directItems) {
            uiAdapter.applyLayoutToActual(item)
        }

        for (item in owner.structuralItems) {
            uiAdapter.applyLayoutToActual(item)
        }
    }

    override fun touchesEnded(touches: Set<*>, withEvent: platform.UIKit.UIEvent?) {
        owner.instructions.firstOrNullIfInstance<OnClick>()
            ?.execute(UIEvent(owner, withEvent))
        super.touchesBegan(touches, withEvent)
    }

}
