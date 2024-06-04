/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry
import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import hu.simplexion.adaptive.foundation.instruction.Trace
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

@AdaptiveEntry
fun browser(
    vararg imports : AdaptiveFragmentFactory,
    rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
    trace : Trace? = null,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : AdaptiveBrowserAdapter {

    return AdaptiveBrowserAdapter(rootContainer).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }

}