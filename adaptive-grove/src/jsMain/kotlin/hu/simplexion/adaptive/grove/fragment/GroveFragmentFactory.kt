/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grove.fragment

import hu.simplexion.adaptive.foundation.fragment.FoundationFragmentFactory
import hu.simplexion.adaptive.ui.common.CommonAdapter

object GroveFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:dpixelinput") { p, i -> GroveDPixelInput(p.adapter as CommonAdapter, p, i) }
    }
}