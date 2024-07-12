/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.OnClick
import hu.simplexion.adaptive.ui.common.instruction.OnCursorDown
import hu.simplexion.adaptive.ui.common.instruction.OnCursorMove
import hu.simplexion.adaptive.ui.common.instruction.OnCursorUp

class EventRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var onClick: OnClick? = null
    var onClickListener : Any? = null

    var onCursorDown: OnCursorDown? = null
    var onCursorDownListener: Any? = null

    var onCursorMove: OnCursorMove? = null
    var onCursorMoveListener: Any? = null

    var onCursorUp: OnCursorUp? = null
    var onCursorUpListener: Any? = null
}