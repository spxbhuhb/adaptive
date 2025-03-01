/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.markdown.model

import `fun`.adaptive.markdown.transform.MarkdownAstTransform

class MarkdownHorizontalRuleAstEntry : MarkdownAstEntry {

    override fun <C, R> accept(visitor: MarkdownAstTransform<C, R>, context: C): R {
        return visitor.visit(this, context)
    }

    override fun toString(): String {
        return this::class.simpleName.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}