/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.platform

import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element

@Suppress("unused") // api
external class ResizeObserverEntry {
    val contentRect: DOMRectReadOnly
    val target: Element
}