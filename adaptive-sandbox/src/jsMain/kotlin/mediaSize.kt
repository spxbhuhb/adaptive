/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.platform.mediaMetrics

@Adaptive
fun mediaSize() {
    val media = mediaMetrics()
    val screenSize = "${media.viewWidth} x ${media.viewHeight}"

    when {
        media.isSmall -> text("small screen ($screenSize)")
        media.isMedium -> text("medium screen ($screenSize)")
        media.isLarge -> text("large screen ($screenSize)")
    }

}