/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grove.fragment

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.grove.sheet.fragment.GroveSheetInner
import `fun`.adaptive.ui.AuiAdapter

object GroveFragmentFactory : FoundationFragmentFactory() {
    init {
        add("grove:dpixelinput") { p, i, s -> GroveDPixelInput(p.adapter as AuiAdapter, p, i) }
        add("grove:sheetinner") { p, i, s -> GroveSheetInner(p.adapter as AuiAdapter, p, i) }
    }
}