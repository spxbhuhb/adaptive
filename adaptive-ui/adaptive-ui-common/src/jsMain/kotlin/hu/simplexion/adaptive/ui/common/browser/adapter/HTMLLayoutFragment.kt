/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.fragment.checkReceiver
import hu.simplexion.adaptive.ui.common.instruction.BackgroundGradient
import hu.simplexion.adaptive.ui.common.instruction.BorderRadius
import hu.simplexion.adaptive.ui.common.instruction.Color
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

/**
 * Common ancestor of HTML layout fragments.
 *
 * IMPORTANT last state variable should be the fragment factory (see [genBuild])
 */
abstract class HTMLLayoutFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize : Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    override val receiver: HTMLDivElement = document.createElement("div") as HTMLDivElement

    class LayoutItem(
        val fragment : AdaptiveUIFragment,
        val receiver : HTMLElement
    )

    val items = mutableListOf<LayoutItem>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(state.size - 1)).apply { create() }
    }

    override fun genPatchInternal(): Boolean {
        if (!haveToPatch(getThisClosureDirtyMask(), instructionIndex)) return true

        for (instruction in instructions) {
            when (instruction) {
                is Color -> {
                    receiver.style.color = instruction.toHexColor()
                }
                is BackgroundGradient -> {
                    receiver.style.background = "linear-gradient(${instruction.degree}deg, ${instruction.start.toHexColor()}, ${instruction.end.toHexColor()})"
                }
                is BorderRadius -> {
                    receiver.style.borderRadius = "${instruction.radius}px"
                }
            }
        }

        return true
    }

    override fun mount() {
        parent?.addActual(this, null)
        super.mount()
        layout()
    }

    abstract fun layout()

    override fun unmount() {
        super.unmount()
        parent?.removeActual(this)
    }

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

        fragment.checkIfInstance<AdaptiveUIFragment>().also { uiFragment ->
            uiFragment.receiver.checkIfInstance<HTMLElement>().also { htmlElementReceiver ->

                items += LayoutItem(uiFragment, htmlElementReceiver)

                if (anchor == null) {
                    receiver.appendChild(htmlElementReceiver)
                } else {
                    checkNotNull(document.getElementById(anchor.id.toString())) { "missing anchor" }
                        .appendChild(htmlElementReceiver)
                }

            }
        }

        if (isMounted) {
            layout()
        }

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        fragment.checkReceiver<HTMLElement>().also { htmlElementReceiver ->
            items.removeAt(items.indexOfFirst { it.fragment.id == fragment.id })
            htmlElementReceiver.remove()
        }

        if (isMounted) {
            layout()
        }

        if (trace) trace("after-removeActual")
    }
}