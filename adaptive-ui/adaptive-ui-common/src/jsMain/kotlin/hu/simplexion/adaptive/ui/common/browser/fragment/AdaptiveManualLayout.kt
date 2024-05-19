/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.fragment

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.ui.common.browser.adapter.AdaptiveBrowserFragment
import hu.simplexion.adaptive.ui.common.commonUI
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

class AdaptiveManualLayout(
    adapter: AdaptiveAdapter,
    parent : AdaptiveFragment,
    index : Int
) : AdaptiveBrowserFragment(adapter, parent, index, 1) {

    override val receiver : HTMLDivElement = document.createElement("div") as HTMLDivElement

    private val content
        get() = getFragmentFactory(1)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        return content.build(this)
    }

    override fun addActual(fragment: AdaptiveFragment) {

    }

    override fun genPatchInternal() : Boolean {
        return true
    }

    companion object : AdaptiveFragmentCompanion {

        override val fragmentType = "$commonUI:AdaptiveManualLayout"

        override fun newInstance(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
            AdaptiveManualLayout(parent.adapter, parent, index)

    }

}