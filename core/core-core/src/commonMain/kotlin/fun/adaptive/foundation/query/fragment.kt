/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.query

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction

/**
 * Find the first fragment that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
fun AdaptiveFragment.firstOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment? {

    for (child in children) {
        if (condition(child)) return child
        if (deep && ! horizontal) {
            child.firstOrNull(deep, horizontal, condition)?.let { return it }
        }
    }

    if (deep && horizontal) {
        for (child in children) {
            child.firstOrNull(deep, horizontal, condition)?.let { return it }
        }
    }

    return null
}

/**
 * Find the first fragment of that matches the condition, throws an exception if no such
 * fragment exists.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException
 */
inline fun AdaptiveFragment.first(
    deep: Boolean = true,
    horizontal: Boolean = true,
    crossinline condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment =
    firstOrNull(deep, horizontal) { condition(it) } ?: throw NoSuchElementException()

/**
 * Find the first fragment of a given class [T], throws an exception if no such
 * fragment exists.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 *
 * @throws NoSuchElementException
 */
inline fun <reified T : Any> AdaptiveFragment.first(
    deep: Boolean = true,
    horizontal: Boolean = true
): T =
    firstOrNull(deep, horizontal) { it is T } as T? ?: throw NoSuchElementException()

/**
 * Find the first fragment of a given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : Any> AdaptiveFragment.firstOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true
): T? =
    firstOrNull(deep, horizontal) { it is T } as T?

/**
 * Find the first fragment which has an instruction of given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveFragment.firstWith(deep: Boolean = true, horizontal: Boolean = true) =
    first(deep, horizontal) { it.instructions.any { i -> i is T } }

/**
 * Find the first fragment which has an instruction of given class [T].
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, check a given level first, children second.
 */
inline fun <reified T : AdaptiveInstruction> AdaptiveFragment.firstWithOrNull(deep: Boolean = true, horizontal: Boolean = true) =
    firstOrNull(deep, horizontal) { it.instructions.any { i -> i is T } }

/**
 * Find the all fragments that matches the given condition.
 *
 * @param deep Deep search, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun AdaptiveFragment.filter(
    matches: MutableList<AdaptiveFragment> = mutableListOf(),
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): MutableList<AdaptiveFragment> {

    for (child in children) {
        if (condition(child)) matches += child
        if (deep && ! horizontal) {
            child.filter(matches, deep, horizontal, condition)
        }
    }

    if (deep && horizontal) {
        for (child in children) {
            child.filter(matches, deep, horizontal, condition)
        }
    }

    return matches
}

/**
 * Returns the single fragment of class [T], or throws an exception
 * if there is no such fragment or there are more than one.
 */
inline fun <reified T : AdaptiveFragment> AdaptiveFragment.single(
    deep: Boolean = true,
    horizontal: Boolean = true
): T =
    single(deep, horizontal) { it is T } as T

/**
 * Returns the single fragment that matches the condition, or throws an exception
 * if there is no such fragment or there are more than one.
 */
fun AdaptiveFragment.single(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment =
    filter(mutableListOf(), deep, horizontal, condition).single()

/**
 * Returns the single fragment that matches the condition or null if there is
 * no such fragment. Throws an exception if there are more than one.
 */
fun AdaptiveFragment.singleOrNull(
    deep: Boolean = true,
    horizontal: Boolean = true,
    condition: (AdaptiveFragment) -> Boolean
): AdaptiveFragment? =
    filter(mutableListOf(), deep, horizontal, condition).singleOrNull()

// -------------------------------------------------------------
// General data collect
// -------------------------------------------------------------

/**
 * Collect data from fragments.
 *
 * @param deep Deep collection, go down in the fragment tree.
 * @param horizontal When [deep] is true, filter a given level first, children second.
 */
fun <T> AdaptiveFragment.collect(
    matches: MutableList<T> = mutableListOf(),
    deep: Boolean = true,
    horizontal: Boolean = true,
    collector: (AdaptiveFragment, MutableList<T>) -> Unit
): MutableList<T> {

    for (child in children) {
        collector(child, matches)
        if (deep && ! horizontal) {
            child.collect(matches, deep, horizontal, collector)
        }
    }

    if (deep && horizontal) {
        for (child in children) {
            child.collect(matches, deep, horizontal, collector)
        }
    }

    return matches
}

// -------------------------------------------------------------
// Finding parent
// -------------------------------------------------------------

fun AdaptiveFragment.firstParentOrNull(
    condition: (AdaptiveFragment) -> Boolean
) : AdaptiveFragment? {
    var current = this.parent
    while (current != null) {
        if (condition(current)) return current
        current = current.parent
    }
    return null
}

fun AdaptiveFragment.firstParent(condition: (AdaptiveFragment) -> Boolean) =
    firstParentOrNull(condition) ?: throw NoSuchElementException()

inline fun <reified T : AdaptiveInstruction > AdaptiveFragment.firstParentWith() =
    firstParent { it.instructions.any { i -> i is T } }

