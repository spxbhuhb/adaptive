/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.common

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveEntry
import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.foundation.instruction.Trace
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

@AdaptiveEntry
fun browser(
    vararg imports : AdaptiveFragmentFactory,
    backend: BackendAdapter = BackendAdapter(),
    rootContainer: HTMLElement = requireNotNull(window.document.body) { "window.document.body is null or undefined" },
    trace : Trace? = null,
    @Adaptive block: (adapter: AbstractCommonAdapter<*, *>) -> Unit
) : CommonAdapter {

    return CommonAdapter(rootContainer, backend).also {
        it.fragmentFactory += imports
        if (trace != null) { it.trace = trace.patterns }
        block(it)
        it.mounted()
    }

}