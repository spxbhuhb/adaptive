/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base

/**
 * Entry point of an Adaptive component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun <BT, AT : AdaptiveAdapter<BT>> adaptive(adapter: AT, block: Adaptive.() -> Unit) : AT {
    adapter.block()
    return adapter
}