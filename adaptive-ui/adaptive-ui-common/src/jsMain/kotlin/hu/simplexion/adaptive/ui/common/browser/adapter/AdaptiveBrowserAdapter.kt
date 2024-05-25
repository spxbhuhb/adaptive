/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.browser.fragment.BrowserFragmentFactory
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.utility.alsoIfInstance
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLElement

open class AdaptiveBrowserAdapter(
    override val rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
) : AdaptiveUIAdapter() {

    override val fragmentFactory = BrowserFragmentFactory

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override fun addActual(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.alsoIfInstance<HTMLLayoutFragment> {

            if (it.frame == null) {
                it.frame = rootContainer.getBoundingClientRect().let { r ->
                    Frame(0f, 0f, r.width.toFloat(), r.height.toFloat())
                }
            }

            rootContainer.appendChild(it.receiver)
        }
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.alsoIfInstance<HTMLLayoutFragment> {
            rootContainer.removeChild(it.receiver)
        }

    }

}