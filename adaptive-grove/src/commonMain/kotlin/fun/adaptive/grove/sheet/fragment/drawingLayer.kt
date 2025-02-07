package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.sheet.model.SheetViewModel

@AdaptiveExpect(grove)
fun drawingLayer(viewModel: SheetViewModel, @Adaptive content : () -> Unit) {
    manualImplementation(viewModel, content)
}