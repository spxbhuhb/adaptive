/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.layout/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.platform.mediaMetrics

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