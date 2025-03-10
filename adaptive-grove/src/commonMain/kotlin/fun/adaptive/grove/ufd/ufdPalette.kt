/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grove.ufd

import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.wsToolPane

@Adaptive
fun ufdPalette() : AdaptiveFragment {

    val context = fragment().wsContext<UfdContext>()
    val items = autoCollection(context.palette) ?: emptyList()

    wsToolPane(context.pane(ufdPalettePaneKey)) {
        column {
            for (item in items) {
                paletteRow(item)
            }
        }
    }

    return fragment()
}

@Adaptive
private fun paletteRow(item: LfmDescendant) {
    val hover = hover()

    draggable {
        transferData { item }
        row {
            rowStyles(false, hover)
            text(item.key) .. noSelect .. textStyles(false, hover)
        }
    }
}