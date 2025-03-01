/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.transform.MarkdownAstTransform

data class MarkdownInlineAstEntry(
    val text: String,
    val bold: Boolean,
    val italic: Boolean,
    val code: Boolean = false,
    val inlineLink: Boolean = false,
    val referenceLink: Boolean = false,
    val referenceDef: Boolean = false,
) : MarkdownAstEntry {

    override fun <C, R> accept(visitor: MarkdownAstTransform<C, R>, context: C): R {
        return visitor.visit(this, context)
    }


}