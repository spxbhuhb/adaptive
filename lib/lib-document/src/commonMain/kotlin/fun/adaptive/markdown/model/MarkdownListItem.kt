/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.visitor.MarkdownTransformer
import `fun`.adaptive.markdown.visitor.MarkdownVisitor

class MarkdownListItem(
    val bullet: Boolean,
    val level: Int,
    var content : MarkdownElement,
    var subList: MarkdownList? = null,
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitListItem(this, data)
    }

    override fun <D> transform(transformer: MarkdownTransformer<D>, data: D): MarkdownElement {
        return transformer.visitListItem(this, data)
    }

    override fun <D> acceptChildren(visitor: MarkdownVisitor<Unit, D>, data: D) {
        content.accept(visitor, data)
        subList?.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: MarkdownTransformer<D>, data: D) {
        content = content.transform(transformer, data)
        subList?.let { subList = it.transform(transformer, data) as MarkdownList }
    }


}