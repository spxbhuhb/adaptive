/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry

/**
 * The entry point of an Adaptive Server component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun server(wait : Boolean = false, @Adaptive block: (adapter : AdaptiveAdapter<*>) -> Unit) : AdaptiveServerAdapter<*> =
    AdaptiveServerAdapter<Any>(wait).also {
        block(it)
        it.mounted()
    }