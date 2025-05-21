package `fun`.adaptive.ui.input.dpixel

import `fun`.adaptive.ui.instruction.DPixel

fun dPixelInputBackend(inputValue : DPixel? = null, builder: DPixelInputViewBackendBuilder.() -> Unit = {  }) =
    DPixelInputViewBackendBuilder(inputValue).apply(builder).toBackend()
