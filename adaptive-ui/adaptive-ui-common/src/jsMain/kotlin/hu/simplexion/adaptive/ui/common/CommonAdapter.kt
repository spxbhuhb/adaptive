/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.ui.common.instruction.AdaptiveUIEvent
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.instruction.SPixel
import hu.simplexion.adaptive.ui.common.platform.BrowserEventListener
import hu.simplexion.adaptive.ui.common.platform.MediaMetrics
import hu.simplexion.adaptive.ui.common.platform.ResizeObserver
import hu.simplexion.adaptive.ui.common.render.DecorationRenderData
import hu.simplexion.adaptive.ui.common.render.EventRenderData
import hu.simplexion.adaptive.ui.common.render.LayoutRenderData
import hu.simplexion.adaptive.ui.common.render.TextRenderData
import hu.simplexion.adaptive.ui.common.support.AbstractContainerFragment
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawSurrounding
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration

open class CommonAdapter(
    final override val rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
) : AbstractCommonAdapter<HTMLElement, HTMLDivElement>() {

    override val fragmentFactory = CommonFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override fun makeContainerReceiver(fragment: AbstractContainerFragment<HTMLElement, HTMLDivElement>): HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    override fun makeStructuralReceiver(fragment: AbstractContainerFragment<HTMLElement, HTMLDivElement>): HTMLDivElement =
        (document.createElement("div") as HTMLDivElement).also { it.style.display = "contents" }

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<AbstractContainerFragment<HTMLElement, HTMLDivElement>> {
            rootContainer.getBoundingClientRect().let { r ->
                val frame = RawFrame(0.0, 0.0, r.width, r.height)

                it.layoutFrame = frame
                it.measure()
                it.layout(frame)

                rootContainer.appendChild(it.receiver)
            }
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<AbstractContainerFragment<HTMLElement, HTMLDivElement>> {
            rootContainer.removeChild(it.receiver)
        }
    }

    override fun addActual(containerReceiver: HTMLDivElement, itemReceiver: HTMLElement) {
        containerReceiver.appendChild(itemReceiver)
    }

    override fun removeActual(itemReceiver: HTMLElement) {
        itemReceiver.remove()
    }

    override fun applyLayoutToActual(fragment: AbstractCommonFragment<HTMLElement>) {
        val layoutFrame = fragment.layoutFrame

        val top = layoutFrame.top
        val left = layoutFrame.left
        val height = layoutFrame.height
        val width = layoutFrame.width

        val style = fragment.receiver.style
        var absolute = false

        style.boxSizing = "border-box"
        val margin = fragment.renderData.layout?.margin ?: RawSurrounding.ZERO

        if (! top.isNaN()) {
            absolute = true
            style.top = top.pxs
        }

        if (! left.isNaN()) {
            absolute = true
            style.left = left.pxs
        }

        style.position = if (absolute) "absolute" else "relative"

        when {
            width == Double.POSITIVE_INFINITY -> style.width = "100%"
            ! width.isNaN() -> style.width = (width - margin.left - margin.right).pxs
        }

        when {
            height == Double.POSITIVE_INFINITY -> style.height = "100%"
            ! height.isNaN() -> style.height = (height - margin.left - margin.right).pxs
        }
    }

    override fun applyRenderInstructions(fragment: AbstractCommonFragment<HTMLElement>) {
        val renderData = fragment.renderData
        val style = fragment.receiver.style

        if (renderData.tracePatterns.isNotEmpty()) {
            fragment.tracePatterns = renderData.tracePatterns
        }

        fragment.instructions.firstOrNullIfInstance<Name>()?.let {
            fragment.receiver.id = it.name
        }

        renderData.layout { it.apply(style) }
        renderData.decoration { it.apply(style) }
        renderData.text { it.apply(style) }
        renderData.event { it.apply(style, fragment) }
    }

    fun LayoutRenderData.apply(style: CSSStyleDeclaration) {
        padding { p ->
            p.left { style.paddingLeft = it.pxs }
            p.top { style.paddingTop = it.pxs }
            p.right { style.paddingRight = it.pxs }
            p.bottom { style.paddingBottom = it.pxs }
        }
        margin { m ->
            m.left { style.marginLeft = it.pxs }
            m.top { style.marginTop = it.pxs }
            m.right { style.marginRight = it.pxs }
            m.bottom { style.marginBottom = it.pxs }
        }
        border { b ->
            style.borderStyle = "solid"
            b.left { style.borderLeftWidth = it.pxs }
            b.top { style.borderTopWidth = it.pxs }
            b.right { style.borderRightWidth = it.pxs }
            b.bottom { style.borderBottomWidth = it.pxs }
        }
    }

    fun DecorationRenderData.apply(style: CSSStyleDeclaration) {
        backgroundColor { style.backgroundColor = it.toHexColor() }
        backgroundGradient { style.background = "linear-gradient(${it.degree}deg, ${it.start.toHexColor()}, ${it.end.toHexColor()})" }

        borderColor { style.borderColor = it.toHexColor() }

        cornerRadius { br ->
            br.topLeft { style.borderTopLeftRadius = it.pxs }
            br.topRight { style.borderTopRightRadius = it.pxs }
            br.bottomLeft { style.borderBottomLeftRadius = it.pxs }
            br.bottomRight { style.borderBottomRightRadius = it.pxs }
        }

        dropShadow { style.filter = "drop-shadow(${it.color.toHexColor()} ${it.offsetX.pxs} ${it.offsetY.pxs} ${it.standardDeviation.pxs})" }
    }

    fun TextRenderData.apply(style: CSSStyleDeclaration) {
        fontName { style.fontFamily = it }
        fontSize { style.fontSize = it.pxs }
        fontWeight { style.fontWeight = it.toString() }
        letterSpacing { style.letterSpacing = "${it}em" }
        align { style.textAlign = it.name.lowercase() }
        wrap { style.setProperty("text-wrap", it.toString().lowercase()) }
        decoration { style.textDecoration = it.value }
        color { style.color = it.toHexColor() }

        if (noSelect == true) {
            style.setProperty("-webkit-user-select", "none")
            style.setProperty("user-select", "none")
        }
    }

    fun EventRenderData.apply(style: CSSStyleDeclaration, fragment: AbstractCommonFragment<HTMLElement>) {

        onClickListener {
            it as BrowserEventListener
            fragment.receiver.removeEventListener("click", it)
        }

        onClick { oc ->
            BrowserEventListener { oc.execute(AdaptiveUIEvent(fragment, it)) }.also {
                onClickListener = it
                fragment.receiver.addEventListener("click", it)
            }
            style.cursor = "pointer"
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

    val DPixel.pxs
        get() = "${value}px"

    val Double.pxs
        inline get() = "${this}px"

    override fun toPx(sPixel: SPixel): Double =
        sPixel.value

    val SPixel.pxs
        get() = "${value}px"

    // ------------------------------------------------------------------------------
    // Media metrics support
    // ------------------------------------------------------------------------------

    override var mediaMetrics = rootContainer.getBoundingClientRect().let { r -> MediaMetrics(r.width, r.height) }

    // FIXME disconnect media observer on adapter dispose
    val resizeObserver = ResizeObserver { entries, _ ->
        entries.firstOrNull()?.let {
            mediaMetrics = MediaMetrics(it.contentRect.width, it.contentRect.height)
            updateMediaMetrics()
        }
    }.also {
        it.observe(rootContainer)
    }

}