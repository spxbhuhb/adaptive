/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation.testing

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry

/**
 * The general entry point of an Adaptive test component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun test(printTrace : Boolean = false, @Adaptive block: (adapter : AdaptiveAdapter) -> Unit) : AdaptiveTestAdapter =
    AdaptiveTestAdapter(printTrace).also {
        block(it)
        it.mounted()
    }