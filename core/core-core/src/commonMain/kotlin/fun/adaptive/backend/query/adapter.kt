/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.backend.query

import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.foundation.query.single
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.BackendFragment
import `fun`.adaptive.backend.builtin.BackendFragmentImpl

// -------------------------------------------------------------
// Inline with implementation class
// -------------------------------------------------------------

/**
 * Find the first backend fragment implementation of class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException if there is no fragment of with implementation of class [T]
 */
inline fun <reified T : BackendFragmentImpl> BackendAdapter.firstImpl(
    deep: Boolean = true,
    horizontal: Boolean = true
): T =
    firstImplOrNull(deep, horizontal) as? T ?: throw NoSuchElementException()

/**
 * Find the first backend fragment implementation of class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : BackendFragmentImpl> BackendAdapter.firstImplOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true
): T? =
    rootFragment
        .firstOrNull(deep, horizontal) { it is BackendFragment && it.impl is T }
        ?.let { (it as BackendFragment) }
        ?.impl as T?

/**
 * Returns the single implementation of class [T], or throws an exception
 * if there is no such implementation or there are more than one.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
inline fun <reified T : BackendFragmentImpl> BackendAdapter.singleImpl(
    deep: Boolean = true,
    horizontal: Boolean = true
): T =
    rootFragment.single(deep, horizontal) { it is BackendFragment && it.impl is T }
        .let { it as BackendFragment }
        .impl as T