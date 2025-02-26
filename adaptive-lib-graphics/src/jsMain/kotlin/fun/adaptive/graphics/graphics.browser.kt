package `fun`.adaptive.graphics

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory

fun AdaptiveAdapter.graphicsBrowser() {
    fragmentFactory += arrayOf(
        CanvasFragmentFactory,
        SvgFragmentFactory
    )
}