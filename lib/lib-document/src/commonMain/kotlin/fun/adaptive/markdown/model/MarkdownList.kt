/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.visitor.MarkdownTransformer
import `fun`.adaptive.markdown.visitor.MarkdownVisitor

class MarkdownList(
    val bullet: Boolean,
    val level: Int,
    val items: MutableList<MarkdownListItem>
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitList(this, data)
    }

    override fun <D> transform(transformer: MarkdownTransformer<D>, data: D): MarkdownElement {
        return transformer.visitList(this, data)
    }

    override fun <D> acceptChildren(visitor: MarkdownVisitor<Unit, D>, data: D) {
        items.forEach { it.accept(visitor, data) }
    }

    override fun <D> transformChildren(transformer: MarkdownTransformer<D>, data: D) {
        for (i in items.indices) {
            items[i] = items[i].transform(transformer, data) as MarkdownListItem
        }
    }

}