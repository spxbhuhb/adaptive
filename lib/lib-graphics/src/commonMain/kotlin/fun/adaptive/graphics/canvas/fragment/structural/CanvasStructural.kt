package `fun`.adaptive.graphics.canvas.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment

abstract class CanvasStructural(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize: Int
) : CanvasFragment(adapter, parent, index, stateSize) {

    override val isStructural
        get() = true

    override fun draw() {
        // children draw is called by the adapter or by a transform
    }

    override fun drawInner() {
        // not called as draw is empty
    }

}