/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.pest_control
import adaptive_grove.generated.resources.redo
import adaptive_grove.generated.resources.undo
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.value.adaptiveValue
import `fun`.adaptive.foundation.value.adaptiveValueStore
import `fun`.adaptive.grove.sheet.SheetEngine.Companion.sheetEngine
import `fun`.adaptive.grove.sheet.fragment.sheet
import `fun`.adaptive.grove.sheet.model.SheetViewModel
import `fun`.adaptive.grove.sheet.operation.Redo
import `fun`.adaptive.grove.sheet.operation.Undo
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.support.statistics.dumpStatistics
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.println

@Adaptive
fun ufdMain() {

    val sheetViewModel = sheetEngine(true)
    val ufdContext = UfdContext()

    grid {
        colTemplate(200.dp, 200.dp, 1.fr, 200.dp)
        maxSize

        palette(ufdContext)

        descendants(sheetViewModel)

        grid {
            rowTemplate(33.dp, 1.fr)

            row {
                maxWidth .. gap { 4.dp } .. borderBottom(colors.outline) .. padding { 4.dp }
                icon(Graphics.undo) .. onClick { sheetViewModel += Undo() }
                icon(Graphics.redo) .. onClick { sheetViewModel += Redo() }
                icon(Graphics.pest_control) .. onClick { adapter().dumpStatistics().println() }
                multiplier(sheetViewModel)
            }

            sheet(sheetViewModel)
        }

        instructions(sheetViewModel)
    }

}

@Adaptive
fun multiplier(viewModel: SheetViewModel) {
    val multiplier = adaptiveValue { viewModel.multiplier }

    if (multiplier != null && multiplier > 1) {
        text("Next keyboard move will be $multiplier pixels") .. textSmall .. semiBoldFont .. textColors.onSurfaceAngry .. alignSelf.bottom
    }
}