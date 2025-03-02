/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.compiler.MarkdownVisitor

class MarkdownParagraph(
    val children: MutableList<MarkdownElement>,
    var closed: Boolean
) : MarkdownElement() {

    override fun <R, D> accept(visitor: MarkdownVisitor<R, D>, data: D): R {
        return visitor.visitParagraph(this, data)
    }

    override fun <D> acceptChildren(visitor: MarkdownVisitor<Unit, D>, data: D) {
        children.forEach { it.accept(visitor, data) }
    }
}