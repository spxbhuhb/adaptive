/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.query

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.server.AdaptiveServerFragment

// -------------------------------------------------------------
// With general condition
// -------------------------------------------------------------

/**
 * Find the first fragment that matches the given condition, throws an exception if no such
 * fragment exists.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException
 */
fun AdaptiveAdapter.first(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment =
    rootFragment.firstOrNull(deep, horizontal, condition) ?: throw NoSuchElementException()

/**
 * Find the first fragment that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
fun AdaptiveAdapter.firstOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment? =
    rootFragment.firstOrNull(deep, horizontal, condition)

/**
 * Find the all fragments that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun AdaptiveAdapter.filter(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): MutableList<AdaptiveFragment> =
    mutableListOf<AdaptiveFragment>().also { rootFragment.filter(it, deep, horizontal, condition) }

/**
 * Find the all fragments that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun AdaptiveAdapter.filter(
    matches: MutableList<AdaptiveFragment> = mutableListOf(),
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): MutableList<AdaptiveFragment> =
    rootFragment.filter(matches, deep, horizontal, condition)

/**
 * Returns the single fragment that matches the condition, or throws an exception
 * if there is no such fragment or there are more than one.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun AdaptiveAdapter.single(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment =
    rootFragment.single(deep, horizontal, condition)

/**
 * Returns the single fragment that matches the condition, or null if there is no
 * such fragment. Throws an exception if there are more than one.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun AdaptiveAdapter.singleOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment? =
    rootFragment.singleOrNull(deep, horizontal, condition)

// -------------------------------------------------------------
// Inline with fragment class
// -------------------------------------------------------------

/**
 * Find the first fragment of a given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException if there is no fragment of class [T]
 */
inline fun <reified T : AdaptiveFragment> AdaptiveAdapter.first(
    deep: Boolean = true,
    horizontal: Boolean = true
): T =
    firstOrNull(deep, horizontal) { it is T } as? T ?: throw NoSuchElementException()

/**
 * Find the first fragment of a given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveFragment> AdaptiveAdapter.firstOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true
): T? =
    firstOrNull(deep, horizontal) { it is T } as T?

/**
 * Returns the single fragment of class [T], or throws an exception
 * if there is no such fragment or there are more than one.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
inline fun <reified T : AdaptiveFragment> AdaptiveAdapter.single(
    deep: Boolean = true,
    horizontal: Boolean = true
): T =
    rootFragment.single(deep, horizontal) { it is T } as T

// -------------------------------------------------------------
// Inline with instruction class
// -------------------------------------------------------------

/**
 * Find the first fragment which has an instruction of given class [T], throws an exception
 * if no such fragment exists.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.firstWith(deep: Boolean = true, horizontal: Boolean = true) =
    rootFragment.firstOrNull(deep, horizontal) { it.instructions.any { i -> i is T } } ?: throw NoSuchElementException()

/**
 * Find the first fragment which has an instruction of given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.firstOrNullWith(deep: Boolean = true, horizontal: Boolean = true) =
    rootFragment.firstOrNull(deep, horizontal) { it.instructions.any { i -> i is T } }

/**
 * Find the first fragment which has an instruction of given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.filterWith(deep: Boolean = true, horizontal: Boolean = true) =
    rootFragment.filter(mutableListOf(), deep, horizontal) { it.instructions.any { i -> i is T } }

/**
 * Find the first fragment which has an instruction of given class [T], throws an exception
 * if no such fragment exists.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.singleWith(deep: Boolean = true, horizontal: Boolean = true) =
    rootFragment.filter(mutableListOf(), deep, horizontal) { it.instructions.any { i -> i is T } }.single()

/**
 * Collect all instructions of type [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveAdapter.collect(deep: Boolean = true, horizontal: Boolean = true) : MutableList<T> =
    rootFragment.collect(mutableListOf(), deep, horizontal) { fragment, matches ->
        matches += fragment.instructions.filterIsInstance<T>()
    }

// -------------------------------------------------------------
// Inline with server implementation class
// -------------------------------------------------------------

/**
 * Find the first server fragment with implementation of a given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : Any> AdaptiveAdapter.firstImpl(
    deep: Boolean = true,
    horizontal: Boolean = true
): T? =
    firstOrNull(deep, horizontal) { f -> (f as? AdaptiveServerFragment)?.let { it.impl is T } ?: false }
        .let { (it as AdaptiveServerFragment).impl as T? }

/**
 * Returns the single fragment of class [T], or throws an exception
 * if there is no such fragment or there are more than one.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
inline fun <reified T : Any> AdaptiveAdapter.singleImpl(
    deep: Boolean = true, // with server, we typically want deep search
    horizontal: Boolean = true
): T =
    rootFragment.single(deep, horizontal) { f -> (f as? AdaptiveServerFragment)?.let { it.impl is T } ?: false }
        .let { (it as AdaptiveServerFragment).impl as T }