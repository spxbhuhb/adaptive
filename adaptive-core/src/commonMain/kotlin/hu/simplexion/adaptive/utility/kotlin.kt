/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

inline fun <reified T> Any.alsoIfInstance(block: (it: T) -> Unit) {
    if (this is T) this.also(block)
}

inline fun <reified T> Any.applyIfInstance(block: T.() -> Unit) {
    if (this is T) this.apply(block)
}

inline fun <reified T> Array<out Any>.firstOrNullIfInstance(): T? {
    return firstOrNull { it is T } as? T
}

inline fun <reified T> Any?.checkIfInstance(): T {
    check(this is T) { "$this is not an instance of ${T::class}" }
    return this
}

suspend inline fun <reified T> waitFor(timeout: Duration, crossinline block: () -> T?): T =
    block() ?: withTimeout(timeout) {
        var value: T?
        do {
            value = block()
            if (value == null) {
                delay(50)
            }
        } while (value == null)
        value
    }