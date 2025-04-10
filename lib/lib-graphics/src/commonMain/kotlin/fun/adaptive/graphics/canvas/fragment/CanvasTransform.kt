package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.utility.alsoIfInstance

@AdaptiveActual(canvas)
class CanvasTransform(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : CanvasFragment(
    adapter, parent, declarationIndex, stateSize()
) {

    val drawItems = mutableListOf<CanvasFragment>()

    // State of transforms has different sizes for different transform types
    // We cannot use `by stateVariable()` here because of that.

     val content: BoundFragmentFactory
        by stateVariable()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)

        // FIXME I think this anonymous fragment is superfluous
        return AdaptiveAnonymous(this, declarationIndex, 1, content).apply { create() }
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "item=$fragment, direct=$direct")

        fragment.alsoIfInstance<CanvasFragment> { itemFragment ->
            drawItems += itemFragment
        }
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "item=$fragment, direct=$direct")

        fragment.alsoIfInstance<CanvasFragment> { itemFragment ->
            drawItems.removeAt(drawItems.indexOfFirst { it.id == fragment.id })
        }
    }

    override fun drawInner() {
        drawItems.forEach { it.draw() }
    }
}