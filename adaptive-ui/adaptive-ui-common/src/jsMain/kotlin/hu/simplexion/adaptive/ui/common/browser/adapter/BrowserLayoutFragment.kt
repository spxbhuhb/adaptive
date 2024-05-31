/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.checkReceiver
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * Common ancestor of HTML layout fragments.
 *
 * IMPORTANT last state variable should be the fragment factory (see [genBuild])
 */
abstract class BrowserLayoutFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : BrowserUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {


    val items = mutableListOf<BrowserUIFragment>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(state.size - 1)).apply { create() }
    }

    override fun makeReceiver(): HTMLElement =
        document.createElement("div") as HTMLElement

    override fun addAnchor(fragment: AdaptiveFragment, higherAnchor: AdaptiveFragment?) {
        (document.createElement("div") as HTMLDivElement).also {
            it.style.display = "contents"
            it.id = fragment.id.toString()
            if (higherAnchor != null) {
                checkNotNull(document.getElementById(higherAnchor.id.toString())) { "missing higher anchor" }.appendChild(it)
            } else {
                receiver.appendChild(it)
            }
        }
    }

    override fun removeAnchor(fragment: AdaptiveFragment) {
        checkNotNull(document.getElementById(fragment.id.toString())) { "missing anchor" }.remove()
    }

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        fragment.checkIfInstance<BrowserUIFragment>().also {

            items += it

            if (anchor == null) {
                receiver.appendChild(it.receiver)
            } else {
                checkNotNull(document.getElementById(anchor.id.toString())) { "missing anchor" }
                    .appendChild(it.receiver)
            }

        }

        if (isMounted) {
            measure()
        }

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        fragment.checkReceiver<HTMLElement>().also { htmlElementReceiver ->
            items.removeAt(items.indexOfFirst { it.id == fragment.id })
            htmlElementReceiver.remove()
        }

        if (isMounted) {
            measure()
        }

        if (trace) trace("after-removeActual")
    }

    fun align() {
        val style = receiver.style

        when (renderData.alignItems) {
            null -> Unit
            AlignItems.Center -> style.alignItems = "center"
            AlignItems.End -> style.alignItems = "end"
            AlignItems.Start -> style.alignItems = "start"
        }

        when (renderData.justifyItems) {
            null -> Unit
            JustifyItems.Center -> style.setProperty("justify-items", "center")
            JustifyItems.End -> style.setProperty("justify-items", "end")
            JustifyItems.Start -> style.setProperty("justify-items", "start")
        }

        when (renderData.alignContent) {
            null -> Unit
            AlignContent.Center -> style.alignContent = "center"
            AlignContent.End -> style.alignContent = "end"
            AlignContent.Start -> style.alignContent = "start"
        }

        when (renderData.justifyContent) {
            null -> Unit
            JustifyContent.Center -> style.setProperty("justify-content", "center")
            JustifyContent.End -> style.setProperty("justify-content", "end")
            JustifyContent.Start -> style.setProperty("justify-content", "start")
        }
    }

}