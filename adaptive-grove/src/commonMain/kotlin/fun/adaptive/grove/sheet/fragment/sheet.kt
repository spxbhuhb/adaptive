package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize

@Adaptive
fun sheet(viewModel: SheetViewModel) {
    box {
        maxSize
        sheetInner(viewModel)
    }
}

@AdaptiveExpect(grove)
fun sheetInner(viewModel: SheetViewModel) {
    manualImplementation(viewModel)
}