/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.compiler.MarkdownVisitor

/**
 * @credit  2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 *          The visitor/transformer concept (and function docs) comes from the Kotlin compiler.
 */
abstract class MarkdownElement {


    /**
     * Runs the provided [visitor] on the Doc subtree with the root at this node.
     *
     * @param visitor The visitor to accept.
     * @param data An arbitrary context to pass to each invocation of [visitor]'s methods.
     * @return The value returned by the topmost `visit*` invocation.
     */
    abstract fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R

    /**
     * Runs the provided [visitor] on subtrees with roots in this node's children.
     *
     * Basically, calls `accept(visitor, data)` on each child of this node.
     *
     * Does **not** run [visitor] on this node itself.
     *
     * @credit  2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
     *
     * @param visitor The visitor for children to accept.
     * @param data An arbitrary context to pass to each invocation of [visitor]'s methods.
     */
    open fun <D> acceptChildren(visitor: MarkdownVisitor<Unit, D>, data: D) = Unit

}