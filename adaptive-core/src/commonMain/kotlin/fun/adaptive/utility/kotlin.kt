/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.utility

inline fun <reified T> Any.alsoIfInstance(block: (it: T) -> Unit) {
    if (this is T) this.also(block)
}

inline fun <reified T> Any.applyIfInstance(block: T.() -> Unit) {
    if (this is T) this.apply(block)
}

inline fun <reified T> List<Any>.firstOrNullIfInstance(): T? {
    return firstOrNull { it is T } as? T
}

inline fun <reified T> Any?.checkIfInstance(): T {
    check(this is T) { "$this is not an instance of ${T::class}" }
    return this
}
