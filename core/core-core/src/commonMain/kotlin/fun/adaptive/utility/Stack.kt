/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.utility

/**
 * Stack as type alias of Mutable List
 * Credit: https://stackoverflow.com/a/61724278/3796844
 */
typealias Stack<T> = MutableList<T>

/**
 * Pushes item to [Stack]
 * @param item Item to be pushed
 */
fun <T> Stack<T>.push(item: T) = add(item)

/**
 * Pops (removes and return) last item from [Stack]
 * @return the last item
 */
fun <T> Stack<T>.pop(): T = removeAt(lastIndex)

/**
 * Pops (removes and return) last item from [Stack] or null if the [Stack] is empty
 *
 * @return the last item or null
 */
fun <T> Stack<T>.popOrNull(): T? = if (isEmpty()) null else removeAt(lastIndex)

/**
 * Peeks (return) last item from [Stack]
 * @return last item if [Stack] is not empty
 * @throws IndexOutOfBoundsException when the stack is empty
 */
fun <T> Stack<T>.peek(): T = this[lastIndex]