/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.view.ViewGroup
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.structural.AdaptiveAnonymous
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveLayoutFragment
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AndroidLayoutFragment(
    adapter: AdaptiveAndroidAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AndroidUIFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize), AdaptiveLayoutFragment<AndroidUIFragment> {

    @Suppress("LeakingThis") // nothing happens during instance creation
    override val receiver = AdaptiveViewGroup(adapter.context, this)

    override val items = mutableListOf<AndroidUIFragment>()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment {
        return AdaptiveAnonymous(adapter, this, declarationIndex, 0, fragmentFactory(state.size - 1)).apply { create() }
    }

    override fun addAnchor(fragment: AdaptiveFragment, higherAnchor: AdaptiveFragment?) {
//            (document.createElement("div") as HTMLDivElement).also {
//                it.style.display = "contents"
//                it.id = fragment.id.toString()
//                if (higherAnchor != null) {
//                    checkNotNull(document.getElementById(higherAnchor.id.toString())) { "missing higher anchor" }.appendChild(it)
//                } else {
//                    receiver.appendChild(it)
//                }
//            }
    }

    override fun removeAnchor(fragment: AdaptiveFragment) {
        //checkNotNull(document.getElementById(fragment.id.toString())) { "missing anchor" }.remove()
    }

    override fun addActual(fragment: AdaptiveFragment, anchor: AdaptiveFragment?) {
        if (trace) trace("addActual", "$fragment $anchor")

        fragment.checkIfInstance<AndroidUIFragment>().also {
            items += it

            if (anchor == null) {
                receiver.addView(it.receiver)
            } else {
//                        checkNotNull(document.getElementById(anchor.id.toString())) { "missing anchor" }
//                            .appendChild(htmlElementReceiver)
            }
        }

        if (isMounted) {
            measure()
        }
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        if (trace) trace("before-removeActual")

//            fragment.checkReceiver<HTMLElement>().also { htmlElementReceiver ->
//                items.removeAt(items.indexOfFirst { it.fragment.id == fragment.id })
//                htmlElementReceiver.remove()
//            }

        if (isMounted) {
            measure()
        }

        if (trace) trace("after-removeActual")
    }

}