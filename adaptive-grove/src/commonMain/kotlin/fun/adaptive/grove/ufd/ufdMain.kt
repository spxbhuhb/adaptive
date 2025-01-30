/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.redo
import adaptive_grove.generated.resources.undo
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.sheet.SheetEngine.Companion.sheetEngine
import `fun`.adaptive.grove.sheet.fragment.sheet
import `fun`.adaptive.grove.sheet.operation.Redo
import `fun`.adaptive.grove.sheet.operation.Undo
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun ufdMain() {

    val sheetViewModel = sheetEngine()
    val ufdContext = UfdContext()

    grid {
        colTemplate(200.dp, 200.dp, 1.fr, 200.dp)
        maxSize

        palette(ufdContext)

        descendants(sheetViewModel)

        grid {
            rowTemplate(24.dp, 1.fr)

            row {
                maxWidth .. gap { 4.dp } .. borderBottom(colors.outline) .. padding { 4.dp }
                icon(Graphics.undo) .. onClick { sheetViewModel += Undo() }
                icon(Graphics.redo) .. onClick { sheetViewModel += Redo() }
            }

            sheet(sheetViewModel)
        }

        instructions(sheetViewModel)
    }

}