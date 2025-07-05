package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class AuiFeedbackPopup(
    adapter: AuiBrowserAdapter,
    parentLayout: AbstractContainer<*, *>,
    val feedbackText: String? = null,
    val feedbackIcon: GraphicsResourceSet? = null
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, null, - 1, 2) {

    // Here we save the CURRENT positioning of the layout parent. Whatever changes
    // later we don't care about. This is fine for a feedback popup which is shown
    // for a very short time period and then disappears forever.

    override val positioningRenderData = parentLayout.renderData

    override val positioningFinalWidth: Double = positioningRenderData.finalWidth
    override val positioningFinalHeight: Double = positioningRenderData.finalHeight
    override val positioningAbsolutePosition: RawPosition = parentLayout.absoluteViewportPosition
    override val positioningRawFrame: RawFrame = positioningRenderData.rawFrame

    override val content: BoundFragmentFactory
        get() = BoundFragmentFactory(this, 100, null)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        if (declarationIndex == 100) {
            return adapter.actualize("lib:feedback-popup-content", parent, 100, 3).also { it.create() }
        }
        return super.genBuild(parent, declarationIndex, flags)
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        if (fragment.declarationIndex == 100) {
            fragment.setStateVariable(1, feedbackText)
            fragment.setStateVariable(2, feedbackIcon)
            return
        }
        super.genPatchDescendant(fragment)
    }

    override fun mount() {
        show() // to let select create the stuff we show
        getOverlay()?.mount()

        receiver.style.position = "fixed"

        adapter.scope.launch {
            delay(1200)
            hide() // to let select drop stuff, it will unmount the overlay
            dispose()
        }
    }

}