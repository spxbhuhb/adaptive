/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.designer.fragment

import hu.simplexion.adaptive.foundation.fragment.FoundationFragmentFactory
import hu.simplexion.adaptive.ui.common.CommonAdapter

object DesignerFragmentFactory : FoundationFragmentFactory() {
    init {
        add("designer:dpixelinput") { p, i -> DesignerDPixelInput(p.adapter as CommonAdapter, p, i) }
    }
}