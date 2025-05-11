/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.visitor.MarkdownTransformer
import `fun`.adaptive.markdown.visitor.MarkdownVisitor

class MarkdownQuote(
    val children: MutableList<MarkdownElement>
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitQuote(this, data)
    }

    override fun <D> transform(transformer: MarkdownTransformer<D>, data: D): MarkdownElement {
        return transformer.visitQuote(this, data)
    }

    override fun <D> acceptChildren(visitor: MarkdownVisitor<Unit, D>, data: D) {
        children.forEach { it.accept(visitor, data) }
    }

    override fun <D> transformChildren(transformer: MarkdownTransformer<D>, data: D) {
        for (i in children.indices) {
            children[i] = children[i].transform(transformer, data)
        }
    }
}
