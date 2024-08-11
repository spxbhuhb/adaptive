/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.platform

import org.w3c.dom.Element

@Suppress("unused") // api
external class ResizeObserver(callback: (Array<ResizeObserverEntry>, ResizeObserver) -> Unit) {
    fun observe(element: Element)
    fun unobserve(element: Element)
    fun disconnect()
}