/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.select

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction

/**
 * Find the first fragment that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
fun AdaptiveAdapter.firstOrNull(
    deep: Boolean = false,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment? =
    rootFragment.firstOrNull(deep, horizontal, condition)

/**
 * Find the first fragment of a given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : Any> AdaptiveAdapter.firstOrNull(
    deep: Boolean = false,
    horizontal: Boolean = true
): T? =
    firstOrNull(deep, horizontal) { it is T } as T?

/**
 * Find the first fragment which has an instruction of given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.firstOrNullWith(deep: Boolean = true, horizontal: Boolean = true) =
    firstOrNull(deep, horizontal) { it.instructions.any { i -> i is T } }

/**
 * Find the all fragments that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun AdaptiveAdapter.filter(
    matches: MutableList<AdaptiveFragment> = mutableListOf(),
    deep: Boolean = false,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): MutableList<AdaptiveFragment> =
    rootFragment.filter(matches, deep, horizontal, condition)

/**
 * Returns the single fragment of class [T], or throws an exception
 * if there is no such fragment or there are more than one.
 */
inline fun <reified T : AdaptiveFragment> AdaptiveAdapter.single(
    deep: Boolean = false,
    horizontal: Boolean = true
): T =
    rootFragment.single(deep, horizontal) { it is T } as T

/**
 * Returns the single fragment that matches the condition, or throws an exception
 * if there is no such fragment or there are more than one.
 */
fun AdaptiveAdapter.single(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment =
    filter(mutableListOf(), deep, horizontal, condition).single()
