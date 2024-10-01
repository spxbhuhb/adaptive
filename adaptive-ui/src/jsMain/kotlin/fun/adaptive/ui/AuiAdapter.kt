/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.Name
import `fun`.adaptive.resource.defaultResourceEnvironment
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.platform.NavSupport
import `fun`.adaptive.ui.platform.ResizeObserver
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.render.BrowserDecorationApplier
import `fun`.adaptive.ui.render.BrowserEventApplier
import `fun`.adaptive.ui.render.model.LayoutRenderData
import `fun`.adaptive.ui.render.model.TextRenderData
import `fun`.adaptive.utility.alsoIfInstance
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration

class AuiAdapter(
    override val rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
    override val backend: BackendAdapter,
    override val transport: ServiceCallTransport = backend.transport
) : AbstractAuiAdapter<HTMLElement, HTMLDivElement>() {

    override val fragmentFactory = AuiFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override fun makeContainerReceiver(fragment: AbstractContainer<HTMLElement, HTMLDivElement>): HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    override fun makeStructuralReceiver(fragment: AbstractContainer<HTMLElement, HTMLDivElement>): HTMLDivElement =
        (document.createElement("div") as HTMLDivElement).also { it.style.display = "contents" }

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<AbstractContainer<HTMLElement, HTMLDivElement>> {
            rootContainer.getBoundingClientRect().let { r ->
                it.computeLayout(r.width, r.height)
                it.placeLayout(0.0, 0.0)
                rootContainer.appendChild(it.receiver)
            }
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<AbstractContainer<HTMLElement, HTMLDivElement>> {
            rootContainer.removeChild(it.receiver)
        }
    }

    override fun addActual(containerReceiver: HTMLDivElement, itemReceiver: HTMLElement) {
        containerReceiver.appendChild(itemReceiver)
    }

    override fun removeActual(itemReceiver: HTMLElement) {
        itemReceiver.remove()
    }

    override fun applyLayoutToActual(fragment: AbstractAuiFragment<HTMLElement>) {
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
        }
    }

    override fun applyRenderInstructions(fragment: AbstractAuiFragment<HTMLElement>) {
        val renderData = fragment.renderData
        val style = fragment.receiver.style

        if (renderData.tracePatterns.isNotEmpty()) {
            fragment.tracePatterns = renderData.tracePatterns
        }

        fragment.instructions.firstOrNullIfInstance<Name>()?.let {
            fragment.receiver.id = it.name
        }

        renderData.layout { it.apply(style) }
        BrowserDecorationApplier.applyTo(fragment)
        renderData.text { it.apply(style) }
        BrowserEventApplier.applyTo(fragment)
    }

    fun LayoutRenderData.apply(style: CSSStyleDeclaration) {
        padding { p ->
            p.start { style.paddingLeft = it.pxs }
            p.top { style.paddingTop = it.pxs }
            p.end { style.paddingRight = it.pxs }
            p.bottom { style.paddingBottom = it.pxs }
        }
        margin { m ->
            m.start { style.marginLeft = it.pxs }
            m.top { style.marginTop = it.pxs }
            m.end { style.marginRight = it.pxs }
            m.bottom { style.marginBottom = it.pxs }
        }
        zIndex {
            style.zIndex = it.toString()
        }
        fixed {
            style.position = "fixed"
        }
    }

    fun TextRenderData.apply(style: CSSStyleDeclaration) {
        (fontName ?: defaultTextRenderData.fontName) { style.fontFamily = it }
        (fontSize ?: defaultTextRenderData.fontSize) { style.fontSize = it.pxs }
        (fontWeight ?: defaultTextRenderData.fontWeight) { style.fontWeight = it.toString() }

        // FIXME text styles in browser
        letterSpacing { style.letterSpacing = "${it}em" }
        wrap { style.setProperty("text-wrap", if (it) "wrap" else "nowrap") }
        color { style.color = it.toHexColor() }

        if (smallCaps) {
            style.fontVariant = "small-caps"
        }

        if (noSelect == true) {
            style.setProperty("-webkit-user-select", "none")
            style.setProperty("user-select", "none")
            style.cursor = "default"
        }
    }

    inline operator fun <reified T : Any> T?.invoke(function: (it: T) -> Unit) {
        if (this != null) {
            function(this)
        }
    }

    override fun openExternalLink(href: String) {
        window.open(href, "_blank")
    }

    override fun toPx(dPixel: DPixel): Double =
        dPixel.value

    override fun toDp(value: Double): DPixel =
        DPixel(value)

    val Double.pxs
        inline get() = "${this}px"

    override fun toPx(sPixel: SPixel): Double =
        sPixel.value

    val SPixel.pxs
        get() = "${value}px"

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
                for (fragment in rootFragment.children) {
                    if (fragment is AbstractAuiFragment<*>) {
                        fragment.computeLayout(r.width, r.height)
                        fragment.placeLayout(0.0, 0.0)
                    }
                }
            }
        }
    }.also {
        it.observe(rootContainer)
    }

    // ------------------------------------------------------------------------------
    // Nav Support
    // ------------------------------------------------------------------------------

    override val navSupport = NavSupport(this)

    // ------------------------------------------------------------------------------
    // Cleanup
    // ------------------------------------------------------------------------------

    // TODO adapter stop mechanism
    override fun stop() {
        resizeObserver.disconnect()
    }
}