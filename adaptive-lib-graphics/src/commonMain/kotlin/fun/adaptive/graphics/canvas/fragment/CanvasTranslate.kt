/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.graphics.canvas.transform.Translate
import `fun`.adaptive.utility.debug

@AdaptiveActual(canvas)
open class CanvasTranslate(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : CanvasFragment(adapter, parent, index, stateSize()) {

    val x1: Double
        by stateVariable()

    val y1: Double
        by stateVariable()

    val content: BoundFragmentFactory
        by stateVariable()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        return AdaptiveAnonymous(parent, declarationIndex, 1, content).apply { create() }
    }

    override fun genPatchInternal(): Boolean {
        super.genPatchInternal()
        return true
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {

    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {

    }

    override fun drawInner() {
        canvas.transform(Translate(x1, y1))
        children.first().children.forEach { if (it is CanvasFragment) it.debug().draw() }
    }

}