/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.redo
import adaptive_grove.generated.resources.undo
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.SheetEngine.Companion.sheetEngine
import `fun`.adaptive.grove.sheet.SheetOuter
import `fun`.adaptive.grove.sheet.operation.Redo
import `fun`.adaptive.grove.sheet.operation.Undo
import `fun`.adaptive.grove.sheet.sheet
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.borderBottom
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun ufdMain() {

    val ufdViewModel = UfdViewModel()
    val sheetViewModel = sheetEngine(trace = true)

    grid {
        colTemplate(200.dp, 200.dp, 1.fr, 200.dp)
        maxSize

        palette(ufdViewModel)

        descendants(sheetViewModel)

        grid {
            rowTemplate(24.dp, 1.fr)

            row {
                maxWidth .. gap { 4.dp } .. borderBottom(colors.outline) .. padding { 4.dp }
                icon(Graphics.undo) .. onClick { sheetViewModel += Undo() }
                icon(Graphics.redo) .. onClick { sheetViewModel += Redo() }
            }

            box {
                maxSize .. SheetOuter()
                sheet(sheetViewModel)
                controlLayers(sheetViewModel, ufdViewModel)
            }

        }

        instructions(sheetViewModel)
    }

}