/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.backend

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveEntry

/**
 * The entry point of a backend component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun backend(wait : Boolean = false, @Adaptive block: (adapter : AdaptiveAdapter) -> Unit) : BackendAdapter =
    BackendAdapter(wait).also {
        block(it)
        it.mounted()
    }