package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.visitor.DocVisitor

@Adat
class DocInlineImage(
    override val style: DocStyleId,
    val text: String,
    val url: String
) : DocInlineElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitInlineImage(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {

    }

}