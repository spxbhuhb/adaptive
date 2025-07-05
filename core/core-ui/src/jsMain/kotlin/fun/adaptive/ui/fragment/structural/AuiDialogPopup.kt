package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.fragment.layout.RawFrame
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.support.UiClose
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

class AuiDialogPopup(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment?
) : AbstractPopup<HTMLElement, HTMLDivElement>(adapter, parent, - 1, 3) {

    // Dialog popups are independent of other fragments.

    override val positioningRenderData
        get() = unsupported()

    override val positioningFinalWidth: Double = uiAdapter.mediaMetrics.viewWidth
    override val positioningFinalHeight: Double = uiAdapter.mediaMetrics.viewHeight
    override val positioningAbsolutePosition: RawPosition = RawPosition(0.0, 0.0)
    override val positioningRawFrame: RawFrame = RawFrame(0.0, 0.0, positioningFinalWidth, positioningFinalHeight)

    override val content: BoundFragmentFactory
        get() = BoundFragmentFactory(this, 100, get(2) as ((AdaptiveFragment, Int) -> AdaptiveFragment?)?)

    override fun patch() {

    }

    override fun patchContent(fragment: AdaptiveFragment) {
        fragment.setStateVariable(1, get(1))
        fragment.setStateVariable(2, UiClose { hide() })
    }

    override fun mount() {
        show() // to let select create the stuff we show
        getOverlay()?.mount()
        receiver.style.position = "fixed"
    }

}