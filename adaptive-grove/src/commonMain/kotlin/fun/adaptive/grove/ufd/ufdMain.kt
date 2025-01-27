package `fun`.adaptive.grove.ufd/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.grove.hydration.lfm.LfmFragment
import `fun`.adaptive.grove.sheet.SheetInner
import `fun`.adaptive.grove.sheet.SheetOuter
import `fun`.adaptive.grove.sheet.sheet
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adat
class Snapshot(
    val before: LfmFragment,
    val operations: List<String>
)

@Adaptive
fun ufdMain() {

    val viewModel = UfdViewModel()

    grid {
        colTemplate(200.dp, 200.dp, 1.fr, 200.dp)
        maxSize

        palette(viewModel)
        descendants(viewModel)
        box {
            maxSize .. SheetOuter()
            sheet(viewModel)
            controlLayers(viewModel)
        }
        instructions(viewModel)
    }

}