/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.transform.MarkdownAstTransform

data class MarkdownHeaderAstEntry(
    val level: Int,
    val children: List<MarkdownInlineAstEntry>
) : MarkdownAstEntry {

    override fun <C, R> accept(visitor: MarkdownAstTransform<C, R>, context: C): R {
        return visitor.visit(this, context)
    }


}