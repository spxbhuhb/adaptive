/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.visitor.MarkdownTransformer
import `fun`.adaptive.markdown.visitor.MarkdownVisitor

class MarkdownInline(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val code: Boolean = false,
    val imageLink: Boolean = false,
    val inlineLink: Boolean = false,
    val referenceLink: Boolean = false,
    val referenceDef: Boolean = false,
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitInline(this, data)
    }

    override fun <D> transform(transformer: MarkdownTransformer<D>, data: D): MarkdownElement {
        return transformer.visitInline(this, data)
    }

    fun isPlainText() =
        ! code && ! imageLink && ! inlineLink && ! referenceLink && ! referenceDef


}