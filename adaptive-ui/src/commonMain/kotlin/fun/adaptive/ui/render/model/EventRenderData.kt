/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render.model

import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.event.OnClick
import `fun`.adaptive.ui.instruction.event.OnMove
import `fun`.adaptive.ui.instruction.event.OnPrimaryDown
import `fun`.adaptive.ui.instruction.event.OnPrimaryUp
import `fun`.adaptive.ui.instruction.event.OnSecondaryDown
import `fun`.adaptive.ui.instruction.event.OnSecondaryUp

class EventRenderData(
    val adapter : AbstractAuiAdapter<*, *>
) {
    var onClick: OnClick? = null
    var onClickListener : Any? = null

    var additionalEvents: Boolean = false
    var noPointerEvents: Boolean? = null

    var onMove: OnMove? = null
    var onMoveListener: Any? = null

    var onPrimaryDown: OnPrimaryDown? = null
    var onPrimaryDownListener: Any? = null

    var onPrimaryUp: OnPrimaryUp? = null
    var onPrimaryUpListener: Any? = null

    var onSecondaryDown: OnSecondaryDown? = null
    var onSecondaryDownListener: Any? = null

    var onSecondaryUp: OnSecondaryUp? = null
    var onSecondaryUpListener: Any? = null
}