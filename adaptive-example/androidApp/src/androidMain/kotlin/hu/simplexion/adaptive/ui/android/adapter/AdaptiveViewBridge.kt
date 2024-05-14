/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.view.View
import android.view.ViewGroup
import hu.simplexion.adaptive.base.AdaptiveBridge
import org.w3c.dom.Node

interface AdaptiveViewBridge : AdaptiveBridge<View> {

    override fun remove(child: AdaptiveBridge<View>) {
        (receiver as ViewGroup).removeView(child.receiver)
    }

    override fun replace(oldChild: AdaptiveBridge<View>, newChild: AdaptiveBridge<View>) {
        throw UnsupportedOperationException()
    }

    override fun add(child: AdaptiveBridge<View>) {
        (receiver as ViewGroup).addView(child.receiver)
    }

}