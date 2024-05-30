/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.browser.fragment.applyRenderInstructions
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.logic.checkReceiver
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

abstract class BrowserUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    override lateinit var receiver: HTMLElement

    abstract fun makeReceiver(): HTMLElement

    override fun create() {
        receiver = makeReceiver()
        super.create()
    }

    override fun layout(proposedFrame : Frame) {
        super.layout(proposedFrame)

        val layoutFrame = renderInstructions.layoutFrame

        check(layoutFrame !== Frame.NaF) { "Missing layout frame in $this" }

        val style = receiver.style

        val point = layoutFrame.point
        val size = layoutFrame.size

        style.position = "absolute"
        style.boxSizing = "border-box"
        style.top = "${point.top}px"
        style.left = "${point.left}px"
        style.width = "${size.width}px"
        style.height = "${size.height}px"
    }

}