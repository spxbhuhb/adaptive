/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.support.AbstractContainerFragment
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawPoint
import hu.simplexion.adaptive.ui.common.support.RawSize
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

open class CommonAdapter(
    override val rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
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

        val point = layoutFrame.point
        val size = layoutFrame.size
        val style = fragment.receiver.style

        style.boxSizing = "border-box"

        if (layoutFrame.point != RawPoint.NaP) {
            style.position = "absolute"
            style.top = "${point.top}px"
            style.left = "${point.left}px"
        } else {
            style.position = "relative"
        }

        if (layoutFrame.size != RawSize.NaS) {
            style.width = "${size.width}px"
            style.height = "${size.height}px"
        }
    }

    override fun applyRenderInstructions(fragment: AbstractCommonFragment<HTMLElement>) {
        with(fragment) {
            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }

            val style = receiver.style

            instructions.firstOrNullIfInstance<Name>()?.let {
                receiver.id = it.name
            }

            with(renderData) {
                // FIXME use classes (when possible) when applying render instructions to HTML element
                backgroundColor?.let { style.backgroundColor = it.toHexColor() }
                backgroundGradient?.let { style.background = "linear-gradient(${it.degree}deg, ${it.start.toHexColor()}, ${it.end.toHexColor()})" }

                border?.let { style.border = "${it.width.value}px solid ${it.color.toHexColor()}" }

                if (borderRadius !== BorderRadius.ZERO) {
                    with(borderRadius) {
                        topLeft.set { style.borderTopLeftRadius = it }
                        topRight.set { style.borderTopRightRadius = it }
                        bottomLeft.set { style.borderBottomLeftRadius = it }
                        bottomRight.set { style.borderBottomRightRadius = it }
                    }
                }

                dropShadow {
                    style.filter = "drop-shadow(${it.color.toHexColor()} ${it.offsetX.pxs} ${it.offsetY.pxs} ${it.standardDeviation.pxs})"
                }

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

                if (textDecoration != TextDecoration.None) {
                    style.textDecoration = textDecoration.value
                }

                if (padding !== Padding.ZERO) {
                    style.paddingLeft = "${padding.left.value}px"
                    style.paddingTop = "${padding.top.value}px"
                    style.paddingRight = "${padding.right.value}px"
                    style.paddingBottom = "${padding.bottom.value}px"
                }

                with (instructedSize) {
                    if (instructedSize.width != DPixel.NaP) {
                        style.width = "${width}px"
                    }
                    if (instructedSize.height != DPixel.NaP) {
                        style.height = "${height}px"
                    }
                }

                if (noSelect) {
                    style.setProperty("-webkit-user-select", "none")
                    style.setProperty("user-select", "none")
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

    inline operator fun <reified T : Any> T?.invoke(function : (it : T) -> Unit) {
        if (this != null) {
            function(this)
        }
    }

    override fun openExternalLink(href: String) {
        window.open(href, "_blank")
    }

    inline fun DPixel.set(setter : (it : String) -> Unit) {
        if (this !== DPixel.NaP) {
            setter("${value}px")
        }
    }

    override fun toPx(dPixel: DPixel): Float =
        dPixel.value

    val DPixel.pxs
        get() = "${value}px"

    override fun toPx(sPixel: SPixel): Float =
        sPixel.value
}