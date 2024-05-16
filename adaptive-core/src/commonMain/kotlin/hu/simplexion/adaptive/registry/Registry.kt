/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.registry

open class Registry<T> {

    private val entries = mutableMapOf<String, T>()

    operator fun set(key: String, value: T) {
        entries[key] = value
    }

    operator fun get(key: String): T? = entries[key]

    operator fun contains(key: String): Boolean = key in entries

    operator fun plusAssign(others: Array<out Registry<T>>) {
        others.forEach { entries.putAll(it.entries) }
    }
}