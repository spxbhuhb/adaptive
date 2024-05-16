/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

/**
 * Fragments that handle the underlying UI implementation such as DOM, implement
 * this interface to make a bridge between the Adaptive fragment and the UI element.
 *
 * @param BT Type of the underlying receiver this bridge handles.
 *           `org.w3c.dom.Node` for example.
 *
 * @property  receiver  The element of the underlying UI that is connected to
 *                      the Adaptive fragment by this bridge.
 */
interface AdaptiveBridge<BT> {

    val receiver: BT

    fun add(child: AdaptiveBridge<BT>)

    fun replace(oldChild: AdaptiveBridge<BT>, newChild: AdaptiveBridge<BT>)

    fun remove(child: AdaptiveBridge<BT>)

}