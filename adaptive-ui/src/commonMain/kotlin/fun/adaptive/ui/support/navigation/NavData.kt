/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.support.navigation

/**
 * Stores parsed version of a navigation URI.
 */
data class NavData(
    val segments: List<String>,
    val query: Map<String, Array<out String>>,
    val tag: String?
) {
    fun sub(consumed: Int) =
        NavData(
            segments.drop(consumed),
            query,
            tag
        )
}