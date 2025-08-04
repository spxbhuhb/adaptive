package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.sheet.SheetViewBackend

@AdaptiveExpect(grove)
fun drawingLayer(controller: SheetViewBackend, content: @Adaptive () -> Unit) {
    manualImplementation(controller, content)
}