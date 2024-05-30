/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.logic.checkReceiver
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

abstract class IosLayoutFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionIndex: Int,
    stateSize: Int
) : IosUIFragment(adapter, parent, declarationIndex, instructionIndex, stateSize) {

    override var receiver = UIView()

    val items = mutableListOf<IosUIFragment>()
    val anchors = mutableMapOf<Long, UIView>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(state.size - 1)).apply { create() }
    }

    override fun measure() {
        for (item in items) {
            item.measure()
        }
    }

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

        fragment.checkIfInstance<IosUIFragment>().also {

            items += it

            if (anchor == null) {
                receiver.addSubview(it.receiver)
            } else {
                checkNotNull(anchors[anchor.id]) { "missing anchor $anchor" }.addSubview(it.receiver)
            }
        }

        if (isMounted) {
            measure()
        }

        if (trace) trace("after-addActual")
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

        fragment.checkReceiver<UIView>().also { uiViewReceiver ->
            items.removeAt(items.indexOfFirst { it.id == fragment.id })
            uiViewReceiver.removeFromSuperview()
        }

        if (isMounted) {
            measure()
        }

        if (trace) trace("after-removeActual")
    }

}