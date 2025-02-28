/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.transform.MarkdownAstTransform

data class MarkdownListAstEntry(
    val bullet: Boolean,
    val level: Int,
    val children: List<MarkdownAstEntry>
) : MarkdownAstEntry {

    override fun <C, R> accept(visitor: MarkdownAstTransform<C, R>, context: C): R {
        return visitor.visit(this, context)
    }


}