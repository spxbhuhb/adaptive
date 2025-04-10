package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.processing.DocVisitor

@Adat
class DocParagraph(
    override val style: DocStyleId,
    val content: List<DocInlineElement>,
    val standalone : Boolean
) : DocBlockElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitParagraph(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {
        content.forEach { it.accept(visitor, data) }
    }

}