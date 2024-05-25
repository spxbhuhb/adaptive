/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.fragment

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory

object ViewFragmentFactory : AdaptiveFragmentFactory() {
    init {
        addAll(
            AdaptiveClickable,
            AdaptiveBox,
            AdaptiveText
        )
    }
}