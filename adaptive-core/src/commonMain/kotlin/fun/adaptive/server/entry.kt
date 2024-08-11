/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.server

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry

/**
 * The entry point of an Adaptive Server component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun server(wait : Boolean = false, @Adaptive block: (adapter : AdaptiveAdapter) -> Unit) : AdaptiveServerAdapter =
    AdaptiveServerAdapter(wait).also {
        block(it)
        it.mounted()
    }