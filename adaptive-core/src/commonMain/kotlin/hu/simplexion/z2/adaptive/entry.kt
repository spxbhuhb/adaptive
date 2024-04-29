/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

/**
 * Entry point of an Adaptive component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun adaptive(block: Adaptive.() -> Unit) : AdaptiveAdapter<*> {
    return AdaptiveAdapterRegistry.adapterFor().apply { block() }
}

/**
 * Entry point of an Adaptive component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
fun <BT> adaptive(adapter: AdaptiveAdapter<BT>, block: Adaptive.() -> Unit) {
    adapter.block()
}