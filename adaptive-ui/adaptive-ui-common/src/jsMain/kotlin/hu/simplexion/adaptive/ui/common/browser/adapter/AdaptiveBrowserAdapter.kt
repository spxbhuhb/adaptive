/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.browser.fragment.BrowserFragmentFactory
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawPoint
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.alsoIfInstance
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class AdaptiveBrowserAdapter(
    override val rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
) : AdaptiveUIAdapter<HTMLDivElement, HTMLElement>() {

    override val fragmentFactory = BrowserFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override fun makeContainerReceiver(fragment: AdaptiveUIContainerFragment<HTMLDivElement, HTMLElement>): HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    override fun makeAnchorReceiver(): HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<AdaptiveUIContainerFragment<HTMLDivElement, HTMLElement>> {
            rootContainer.getBoundingClientRect().let { r ->
                val frame = RawFrame(0f, 0f, r.width.toFloat(), r.height.toFloat())

                it.layoutFrame = frame
                it.measure()
                it.layout(frame)

                rootContainer.appendChild(it.receiver)
            }
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<AdaptiveUIContainerFragment<HTMLDivElement, HTMLElement>> {
            rootContainer.removeChild(it.receiver)
        }
    }

    override fun addActual(containerReceiver: HTMLDivElement, itemReceiver: HTMLElement) {
        containerReceiver.appendChild(itemReceiver)
    }

    override fun removeActual(itemReceiver: HTMLElement) {
        itemReceiver.remove()
    }

    override fun applyLayoutToActual(fragment: AdaptiveUIFragment<HTMLElement>) {
        val layoutFrame = fragment.layoutFrame

        val point = layoutFrame.point
        val size = layoutFrame.size
        val style = fragment.receiver.style

        style.boxSizing = "border-box"

        if (layoutFrame.point != RawPoint.NaP) {
            style.position = "absolute"
            style.top = "${point.top}px"
            style.left = "${point.left}px"
        }

        if (layoutFrame.size != RawSize.NaS) {
            style.width = "${size.width}px"
            style.height = "${size.height}px"
        }
    }

    override fun applyRenderInstructions(fragment: AdaptiveUIFragment<HTMLElement>) {
        with(fragment) {
            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }

            val style = receiver.style

            with(renderData) {
                // FIXME use classes (when possible) when applying render instructions to HTML element
                backgroundColor?.let { style.backgroundColor = it.toHexColor() }
                backgroundGradient?.let { style.background = "linear-gradient(${it.degree}deg, ${it.start.toHexColor()}, ${it.end.toHexColor()})" }

                border?.let { style.border = "${it.width.value}px solid ${it.color.toHexColor()}" }
                borderRadius?.let { style.borderRadius = "${it.value}px" }

                color?.let { style.color = it.toHexColor() }

                fontSize?.let { style.fontSize = "${it.value}px" }
                fontWeight?.let { style.fontWeight = it.toString() }
                letterSpacing?.let { style.letterSpacing = "${it}em" }

                textAlign?.let {
                    style.textAlign = it.name.lowercase()
                }

                textWrap?.let {
                    style.setProperty("text-wrap", it.toString().lowercase())
                }

                padding?.let { p ->
                    style.paddingLeft = "${p.left.value}px"
                    style.paddingTop = "${p.top.value}px"
                    style.paddingRight = "${p.right.value}px"
                    style.paddingBottom = "${p.bottom.value}px"
                }

                instructedSize?.let {
                    style.width = "${it.width}px"
                    style.height = "${it.height}px"
                }

                onClick?.let { oc ->
                    // FIXME handling of onClick is wrong on so many levels
                    receiver.addEventListener(
                        "click",
                        { oc.execute(AdaptiveUIEvent(fragment, it)) }
                    )
                    style.cursor = "pointer"
                }
            }
        }
    }

    override fun openExternalLink(href: String) {
        window.open(href, "_blank")
    }

    override fun toPx(dPixel: DPixel): Float =
        dPixel.value

    override fun toPx(sPixel: SPixel): Float =
        sPixel.value
}