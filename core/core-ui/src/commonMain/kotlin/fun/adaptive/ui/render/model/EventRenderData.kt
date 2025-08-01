/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.instruction.event.*

@Suppress("EqualsOrHashCode")
class EventRenderData(
    val adapter: DensityIndependentAdapter
) {
    var onClick: OnClick? = null
    var onClickListener: Any? = null

    var additionalEvents: Boolean = false
    var pointerEvents: PointerEvents? = null

    var onDoubleClick: OnDoubleClick? = null
    var onDoubleClickListener: Any? = null

    var onPointerMove: OnPointerMove? = null
    var onPointerMoveListener: Any? = null

    var onPointerLeave: OnPointerLeave? = null
    var onPointerLeaveListener: Any? = null

    var onPrimaryDown: OnPrimaryDown? = null
    var onPrimaryDownListener: Any? = null

    var onPrimaryUp: OnPrimaryUp? = null
    var onPrimaryUpListener: Any? = null

    var onSecondaryDown: OnSecondaryDown? = null
    var onSecondaryDownListener: Any? = null

    var onSecondaryUp: OnSecondaryUp? = null
    var onSecondaryUpListener: Any? = null

    var onDrop: OnDrop? = null
    var onDropListener: Any? = null

    var onKeyDown: OnKeyDown? = null
    var onKeyDownListener: Any? = null

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other !is EventRenderData) return false

        // event listeners will not be the same until the render data is actually applied

        if (other.onClick != this.onClick) return false
        if (other.additionalEvents != this.additionalEvents) return false
        if (other.onDoubleClick != this.onDoubleClick) return false
        if (other.onPointerMove != this.onPointerMove) return false
        if (other.onPointerLeave != this.onPointerLeave) return false
        if (other.onPrimaryDown != this.onPrimaryDown) return false
        if (other.onPrimaryUp != this.onPrimaryUp) return false
        if (other.onSecondaryDown != this.onSecondaryDown) return false
        if (other.onSecondaryUp != this.onSecondaryUp) return false
        if (other.onDrop != this.onDrop) return false
        if (other.onKeyDown != this.onKeyDown) return false

        return true
    }

    fun copyListeners(previous: EventRenderData) {
        onClickListener = previous.onClickListener
        if (previous.additionalEvents) {
            additionalEvents = true
            onDoubleClickListener = previous.onDoubleClickListener
            onPointerMoveListener = previous.onPointerMoveListener
            onPointerLeaveListener = previous.onPointerLeaveListener
            onPrimaryDownListener = previous.onPrimaryDownListener
            onPrimaryUpListener = previous.onPrimaryUpListener
            onSecondaryDownListener = previous.onSecondaryDownListener
            onSecondaryUpListener = previous.onSecondaryUpListener
            onDropListener = previous.onDropListener
            onKeyDownListener = previous.onKeyDownListener
        }
    }
}