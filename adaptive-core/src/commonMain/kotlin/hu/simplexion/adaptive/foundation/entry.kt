/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.foundation

/**
 * The general entry point of an Adaptive component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun <AT : AdaptiveAdapter> adaptive(adapter: AT, @Adaptive block: (adapter : AdaptiveAdapter) -> Unit) : AT {
    block(adapter)
    adapter.mounted()
    return adapter
}