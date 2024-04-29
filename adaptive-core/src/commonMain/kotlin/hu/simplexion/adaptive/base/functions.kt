/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.base

fun replacedByPlugin(message: String): Nothing {
    throw IllegalStateException(message)
}

/**
 * Call to this function indicates that the adaptive class of this fragment is written
 * manually. This is typical for bridge-dependent classes that interact directly with
 * the underlying UI. Most probably you'll find the class right under the function.
 */
@Suppress("UnusedReceiverParameter")
fun Adaptive.manualImplementation(vararg arguments : Any?) {
    throw IllegalStateException("Manual implementation function should never be called. Is the Adaptive plugin missing? ${arguments.contentToString()}")
}

fun adapter() : AdaptiveAdapter<*> {
    replacedByPlugin("gets the adapter of the fragment")
}

fun fragment() : AdaptiveFragment<*> {
    replacedByPlugin("gets the fragment")
}

fun <T : AdaptiveTransformInterface> thisState() : T {
    replacedByPlugin("gets the fragment as a transfrom interface")
}