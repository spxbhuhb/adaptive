/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.palette
import `fun`.adaptive.auto.api.autoCollection
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun palette(viewModel: UfdContext) {
    val items = autoCollection(viewModel.palette) ?: emptyList()

    column {
        maxSize .. borderRight(colors.outline)

        areaTitle(Strings.palette, Graphics.palette)

        for (item in items) {
            draggable {
                transferData { item }
                text(item.key)
            }
        }
    }
}