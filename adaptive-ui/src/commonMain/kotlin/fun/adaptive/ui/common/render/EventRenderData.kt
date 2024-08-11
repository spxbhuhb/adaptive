/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.render

import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.instruction.*

class EventRenderData(
    val adapter : AbstractCommonAdapter<*, *>
) {
    var onClick: OnClick? = null
    var onClickListener : Any? = null

    var additionalEvents: Boolean = false

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