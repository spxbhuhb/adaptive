/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.visitor.MarkdownTransformer
import `fun`.adaptive.markdown.visitor.MarkdownVisitor

class MarkdownCodeFence(
    val language: String?,
    val content: String
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitCodeFence(this, data)
    }

    override fun <D> transform(transformer: MarkdownTransformer<D>, data: D): MarkdownElement {
       return transformer.visitCodeFence(this, data)
    }

}