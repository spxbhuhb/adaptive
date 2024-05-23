/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.fragment.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.fragment.checkReceiver
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIView

abstract class IOSLayoutFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionIndex: Int,
    stateSize: Int
) : AdaptiveUIFragment(adapter, parent, declarationIndex, instructionIndex, stateSize) {

    override val receiver = UIView()

    @OptIn(ExperimentalForeignApi::class)
    override var frame
        get() = uiInstructions.frame
        set(v) {
            uiInstructions.frame = v
            receiver.setFrame(v.toCGRect())
        }

    class LayoutItem(
        val fragment : AdaptiveUIFragment,
        val receiver : UIView
    )

    val items = mutableListOf<LayoutItem>()
    val anchors = mutableMapOf<Long, UIView>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(state.size - 1)).apply { create() }
    }

    override fun genPatchInternal(): Boolean = true

    override fun mount() {
        super.mount()
        layout()
    }

    abstract fun layout()

    override fun addAnchor(fragment: AdaptiveFragment, higherAnchor: AdaptiveFragment?) {
        UIView().also {
            if (higherAnchor != null) {
                checkNotNull(anchors[higherAnchor.id]) { "missing higher anchor $fragment $higherAnchor" }.addSubview(it)
            } else {
                receiver.addSubview(it)
            }
            anchors[fragment.id] = it
        }
    }

    override fun removeAnchor(fragment: AdaptiveFragment) {
        anchors[fragment.id]?.removeFromSuperview()
    }

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("before-addActual")

        fragment.checkIfInstance<AdaptiveUIFragment>().also { uiFragment ->
            uiFragment.receiver.checkIfInstance<UIView>().also { uiViewReceiver ->

                items += LayoutItem(uiFragment, uiViewReceiver)

                if (anchor == null) {
                    receiver.addSubview(uiViewReceiver)
                } else {
                    checkNotNull(anchors[anchor.id]) { "missing anchor $anchor" }.addSubview(uiViewReceiver)
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

        fragment.checkReceiver<UIView>().also { uiViewReceiver ->
            items.removeAt(items.indexOfFirst { it.fragment.id == fragment.id })
            uiViewReceiver.removeFromSuperview()
        }

        if (isMounted) {
            layout()
        }

        if (trace) trace("after-removeActual")
    }

}