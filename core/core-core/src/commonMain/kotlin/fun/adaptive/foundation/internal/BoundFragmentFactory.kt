/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.internal

import `fun`.adaptive.foundation.AdaptiveFragment

/**
 * Instances of [BoundFragmentFactory](class://) are stored in state variables when an
 * adaptive function has a parameter that is another adaptive function.
 *
 * There are two modes: inline and reference.
 *
 * ## Inline mode
 *
 * Inline mode is used for lambdas where the fragment model is declared as a lambda
 * in an [original function](def://).
 *
 * In inline mode:
 *
 * - the declaration index is the actual declaration index in the declaring fragment
 * - the [functionReference](parameter://) parameter is `null`.
 *
 * ```kotlin
 * @Adaptive
 * fun someFun() {
 *     column {
 *         text("Hello World!")
 *     }
 * }
 * ```
 *
 * ## Reference mode
 *
 * Reference mode is used when the fragment model is passes as a function reference.
 *
 * In reference mode:
 *
 * - [declarationIndex](parameter://) is typically manually handled hard-coded value such as -1 or 100
 * - the [functionReference](parameter://) contains the Kotlin function reference to call from [genBuild](function://)
 */
class BoundFragmentFactory(
    val declaringFragment: AdaptiveFragment,
    val declarationIndex: Int,
    val functionReference: ((parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment?)?
) {

    fun build(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        if (functionReference != null) {
            functionReference(parent, declarationIndex)?.also { it.create() }
        } else {
            declaringFragment.genBuild(parent, this.declarationIndex, flags)
        }

    override fun toString() =
        "BoundFragmentFactory(${declaringFragment.id},$declarationIndex)"
}