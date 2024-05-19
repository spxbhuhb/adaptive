/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.android.basic

import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory

object ViewFragmentFactory : AdaptiveFragmentFactory() {
    init {
        addAll(AdaptiveText)
    }
}