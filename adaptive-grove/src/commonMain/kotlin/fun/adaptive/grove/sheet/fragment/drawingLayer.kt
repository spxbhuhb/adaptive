package `fun`.adaptive.grove.sheet.fragment

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grove.grove
import `fun`.adaptive.grove.sheet.SheetViewController

@AdaptiveExpect(grove)
fun drawingLayer(controller: SheetViewController, @Adaptive content : () -> Unit) {
    manualImplementation(controller, content)
}