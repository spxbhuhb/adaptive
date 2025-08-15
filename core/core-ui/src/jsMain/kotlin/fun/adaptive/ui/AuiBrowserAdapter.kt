/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.resource.defaultResourceEnvironment
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.event.UIEvent
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.platform.ResizeObserver
import `fun`.adaptive.ui.platform.applyCustomScrollBar
import `fun`.adaptive.ui.platform.getScrollbarWidth
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.render.*
import `fun`.adaptive.utility.alsoIfInstance
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.DOMRect
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.pointerevents.PointerEvent

class AuiBrowserAdapter(
    override val rootContainer : HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
    override val backend : BackendAdapter,
    override val transport : ServiceCallTransport = backend.transport
) : AbstractAuiAdapter<HTMLElement, HTMLDivElement>() {

    override val fragmentFactory = AuiFragmentFactory

    override val dispatcher : CoroutineDispatcher
        get() = Dispatchers.Default

    /**
     * Fragments which are over the main root fragment, such as dialog boxes.
     */
    val otherRootFragments = mutableListOf<AdaptiveFragment>()

    val haveToHackScrollbar = ! GlobalRuntimeContext.platform.isMac

    // the order is important here, apply the style first, measure after
    override val scrollBarSize : Double = getScrollbarWidth()

    override fun makeContainerReceiver(fragment : AbstractContainer<HTMLElement, HTMLDivElement>) : HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    override fun makeStructuralReceiver(fragment : AbstractContainer<HTMLElement, HTMLDivElement>) : HTMLDivElement =
        (document.createElement("div") as HTMLDivElement).also { it.style.display = "contents" }

    override fun addActualRoot(fragment : AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<AbstractAuiFragment<HTMLElement>> {
            rootContainer.getBoundingClientRect().let { r ->
                computeAndPlace(it, r)
                rootContainer.appendChild(it.receiver)
                executeLayoutTasks()
            }
        }

        otherRootFragments += fragment
    }

    override fun removeActualRoot(fragment : AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<AbstractAuiFragment<HTMLElement>> {
            rootContainer.removeChild(it.receiver)
        }

        otherRootFragments -= fragment
    }

    // Adding and removing elements is quite expensive and may cause a browser
    // layout / redraw, which in turn results in flickering. To avoid this,
    // we postpone adding and removing to the live DOM until after the
    // layouts are calculated and styles are applied.

    class ActualOp(
        val containerReceiver : HTMLElement?,
        val itemReceiver : HTMLElement,
        val add : Boolean
    )

    val actualOps = mutableListOf<ActualOp>()

    override fun addActual(containerReceiver : HTMLDivElement, itemReceiver : HTMLElement) {
        // When the container is not part of the live DOM, we can freely add children.
        // Otherwise, we better postpone the operation until after the layouts are calculated.
        if (! containerReceiver.isConnected) {
            containerReceiver.appendChild(itemReceiver)
        } else {
            actualOps += ActualOp(containerReceiver, itemReceiver, true)
        }
    }

    override fun removeActual(itemReceiver : HTMLElement) {
        // When the item is not part of the live DOM, we can freely remove it.
        // Otherwise, we better postpone the operation until after the layouts are calculated.
        if (! itemReceiver.isConnected) {
            itemReceiver.remove()
        } else {
            actualOps += ActualOp(null, itemReceiver, false)
        }
    }

    override fun closePatchBatch() {
        window.requestAnimationFrame {
            super.closePatchBatch()

            // Execute the DOM additions/removals in a batch.
            actualOps.forEach {
                if (it.add) {
                    it.containerReceiver?.appendChild(it.itemReceiver)
                } else {
                    it.itemReceiver.remove()
                }
            }
            actualOps.clear()
        }
    }

    override fun applyLayoutToActual(fragment : AbstractAuiFragment<HTMLElement>) {
        val data = fragment.renderData

        if (fragment.isStructural) {
            return
        }

        val top = data.finalTop
        val left = data.finalLeft
        val height = data.finalHeight
        val width = data.finalWidth

        val margin = data.layout?.margin ?: RawSurrounding.ZERO
        val parentLayout = data.layoutFragment?.renderData?.layout
        val parentMargin = parentLayout?.margin ?: RawSurrounding.ZERO
        val parentBorder = parentLayout?.border ?: RawSurrounding.ZERO

        val style = fragment.receiver.style

        // these two are in index.html for now
        // style.boxSizing = "border-box"
        // style.position = "absolute"
        style.top = (top - parentBorder.top - parentMargin.top).pxs
        style.left = (left - parentBorder.start - parentMargin.start).pxs
        style.width = (width - margin.start - margin.end).pxs
        style.height = (height - margin.top - margin.bottom).pxs

        data.container {
            if (it.horizontalScroll) style.overflowX = "auto"
            if (it.verticalScroll) style.overflowY = "auto"
            if (it.horizontalScroll || it.verticalScroll) {
                if (haveToHackScrollbar) {
                    applyCustomScrollBar(fragment.receiver)
                }
            }
        }
    }

    override fun applyLayoutIndependent(fragment : AbstractAuiFragment<HTMLElement>) {
        val renderData = fragment.renderData

        if (renderData.tracePatterns.isNotEmpty()) {
            fragment.tracePatterns = renderData.tracePatterns
        }

        fragment.instructions.firstInstanceOfOrNull<Name>()?.let {
            fragment.receiver.id = it.name
        }

        BrowserLayoutApplier.applyTo(fragment)
        BrowserDecorationApplier.applyTo(fragment)
        BrowserTextApplier.applyTo(fragment)
        BrowserEventApplier.applyTo(fragment)
        BrowserInputApplier.applyTo(fragment)
    }

    inline operator fun <reified T : Any> T?.invoke(function : (it : T) -> Unit) {
        if (this != null) {
            function(this)
        }
    }

    override fun openExternalLink(href : String) {
        window.open(href, "_blank")
    }

    override fun toPx(dPixel : DPixel) : Double =
        dPixel.value

    override fun toDp(value : Double) : DPixel =
        DPixel(value)

    val Double.pxs
        inline get() = "${this}px"

    override fun toPx(sPixel : SPixel) : Double =
        sPixel.value

    // ------------------------------------------------------------------------------
    // Media metrics support
    // ------------------------------------------------------------------------------

    override var mediaMetrics = rootContainer.getBoundingClientRect().let { r ->
        MediaMetrics(r.width, r.height, defaultResourceEnvironment.theme, manualTheme)
    }

    val resizeObserver = ResizeObserver { entries, _ ->
        entries.firstOrNull()?.let {
            mediaMetrics = MediaMetrics(
                it.contentRect.width,
                it.contentRect.height,
                defaultResourceEnvironment.theme,
                manualTheme
            )

            updateMediaMetrics()

            rootContainer.getBoundingClientRect().let { r ->
                computeAndPlace(rootFragment, r)
                otherRootFragments.forEach { f -> computeAndPlace(f, r) }
                executeLayoutTasks()
            }
        }
    }.also {
        it.observe(rootContainer)
    }

    fun computeAndPlace(fragment : AdaptiveFragment, rect : DOMRect) {
        if (fragment is AbstractAuiFragment<*>) {
            fragment.computeLayout(0.0, rect.width, 0.0, rect.height)
            fragment.placeLayout(fragment.renderData.finalTop, fragment.renderData.finalTop)
        }
    }

    // ------------------------------------------------------------------------------
    // Focus support
    // ------------------------------------------------------------------------------

    override fun focus(fragment : AbstractAuiFragment<*>) {
        (fragment.receiver as? HTMLElement)?.focus()
    }

    // ------------------------------------------------------------------------------
    // Scroll support
    // ------------------------------------------------------------------------------

    override fun scrollPosition(fragment : AdaptiveFragment) : RawPosition? {
        val receiver = expectUiFragment<HTMLElement>(fragment)?.receiver ?: return null
        return RawPosition(receiver.scrollTop, receiver.scrollLeft)
    }

    override fun scrollTo(fragment : AdaptiveFragment, position : RawPosition) {
        val receiver = expectUiFragment<HTMLElement>(fragment)?.receiver ?: return
        receiver.scrollTo(position.top, position.left)
    }

    override fun scrollIntoView(fragment : AdaptiveFragment, alignment : Alignment) {
        val fragment = expectUiFragment<HTMLElement>(fragment) ?: return

        val (layoutContainer, position) = scrollState(fragment)
        if (position == null) return

        val layoutRenderData = layoutContainer?.renderData ?: return
        val viewportWidth = layoutRenderData.finalWidth
        val viewportHeight = layoutRenderData.finalHeight

        val layoutScrollPosition = scrollPosition(layoutContainer) ?: RawPosition(0.0, 0.0)

        val elementWidth = fragment.renderData.finalWidth
        val elementHeight = fragment.renderData.finalHeight

        val top = position.top
        val left = position.left

        val isVerticallyVisible = top >= layoutScrollPosition.top &&
            (top + elementHeight) <= (layoutScrollPosition.top + viewportHeight)
        val isHorizontallyVisible = left >= layoutScrollPosition.left &&
            (left + elementWidth) <= (layoutScrollPosition.left + viewportWidth)

        if (! isVerticallyVisible || ! isHorizontallyVisible) {

            val targetTop = if (elementHeight > viewportHeight) {
                top
            } else {
                when (alignment) {
                    Alignment.Start -> top - layoutRenderData.surroundingVertical
                    Alignment.Center -> top + elementHeight / 2 - viewportHeight / 2
                    Alignment.End -> top + elementHeight - viewportHeight + layoutRenderData.surroundingVertical
                }
            }

            val targetLeft = if (elementWidth <= viewportWidth) {
                left + elementWidth / 2 - viewportWidth / 2
            } else {
                left
            }

            layoutContainer.receiver.scrollTo(targetLeft, targetTop)
        }
    }

    // ------------------------------------------------------------------------------
    // Pointer capture support
    // ------------------------------------------------------------------------------

    override fun acquirePointerCapture(event : UIEvent) {
        val element = (event.fragment.receiver as? HTMLElement) ?: return
        val nativeEvent = event.nativeEvent as? PointerEvent ?: return
        element.setPointerCapture(nativeEvent.pointerId)
    }

    override fun releasePointerCapture(event : UIEvent) {
        val element = (event.fragment.receiver as? HTMLElement) ?: return
        val nativeEvent = event.nativeEvent as? PointerEvent ?: return
        element.releasePointerCapture(nativeEvent.pointerId)
    }

    // ------------------------------------------------------------------------------
    // Cleanup
    // ------------------------------------------------------------------------------

    // TODO adapter stop mechanism
    override fun stop() {
        resizeObserver.disconnect()
    }
}