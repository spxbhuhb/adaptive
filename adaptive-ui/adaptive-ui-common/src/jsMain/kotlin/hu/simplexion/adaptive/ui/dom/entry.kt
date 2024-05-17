/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.dom

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry
import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory
import org.w3c.dom.Node

/**
 * The entry point of an Adaptive browser component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun browser(
    vararg imports : AdaptiveFragmentFactory,
    trace : Boolean = false,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : AdaptiveDOMAdapter {

    return AdaptiveDOMAdapter().also {
        it.trace = trace
        it.fragmentFactory += imports
        block(it)
        it.mounted()
    }

}