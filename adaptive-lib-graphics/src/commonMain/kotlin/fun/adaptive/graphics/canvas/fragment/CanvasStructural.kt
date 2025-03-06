/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment

open class CanvasStructural(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize: Int
) : CanvasFragment(adapter, parent, index, stateSize) {

    override fun drawInner() {

    }

}
