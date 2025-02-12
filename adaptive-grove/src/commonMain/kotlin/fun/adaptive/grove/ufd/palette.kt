/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.palette
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun palette(viewModel: UfdContext) {
    val items = autoCollection(viewModel.palette) ?: emptyList()

    grid {
        maxSize .. borderRight(colors.outline)
        rowTemplate(udfTheme.headerHeight, 1.fr)

        areaTitle(Strings.palette, Graphics.palette)

        column {
            maxSize .. scroll .. padding { 4.dp }
            for (item in items) {
                paletteRow(item)
            }
        }
    }
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