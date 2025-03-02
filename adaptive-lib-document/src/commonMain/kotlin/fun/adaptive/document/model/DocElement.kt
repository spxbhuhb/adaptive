package `fun`.adaptive.document.model

import `fun`.adaptive.document.processing.DocVisitor

/**
 * @credit  2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 *          The visitor/transformer concept (and function docs) comes from the Kotlin compiler.
 */
abstract class DocElement {

    abstract val style: DocStyleId

    /**
     * Runs the provided [visitor] on the Doc subtree with the root at this node.
     *
     * @param visitor The visitor to accept.
     * @param data An arbitrary context to pass to each invocation of [visitor]'s methods.
     * @return The value returned by the topmost `visit*` invocation.
     */
    abstract fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R

    /**
     * Runs the provided [transformer] on the Doc subtree with the root at this node.
     *
     * @param transformer The transformer to use.
     * @param data An arbitrary context to pass to each invocation of [transformer]'s methods.
     * @return The transformed node.
     */
    // abstract fun <D> transform(transformer: DocTransformer<D>, data: D): DocElement

    /**
     * Runs the provided [visitor] on subtrees with roots in this node's children.
     *
     * Basically, calls `accept(visitor, data)` on each child of this node.
     *
     * Does **not** run [visitor] on this node itself.
     *
     * @param visitor The visitor for children to accept.
     * @param data An arbitrary context to pass to each invocation of [visitor]'s methods.
     */
    abstract fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D)

    /**
     * Recursively transforms this node's children *in place* using [transformer].
     *
     * Basically, executes `this.child = this.child.transform(transformer, data)` for each child of this node.
     *
     * Does **not** run [transformer] on this node itself.
     *
     * @param transformer The transformer to use for transforming the children.
     * @param data An arbitrary context to pass to each invocation of [transformer]'s methods.
     */
    // abstract fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D)

}