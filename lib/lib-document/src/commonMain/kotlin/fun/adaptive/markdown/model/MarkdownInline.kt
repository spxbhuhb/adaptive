/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.compiler.MarkdownVisitor

class MarkdownInline(
    val text: String,
    val bold: Boolean,
    val italic: Boolean,
    val code: Boolean = false,
    val imageLink: Boolean = false,
    val inlineLink: Boolean = false,
    val referenceLink: Boolean = false,
    val referenceDef: Boolean = false,
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitInline(this, data)
    }

}