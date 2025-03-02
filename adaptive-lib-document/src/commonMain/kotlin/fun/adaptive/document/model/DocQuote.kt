package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.processing.DocVisitor

@Adat
class DocQuote(
    override val style: DocStyleId,
    val content: List<DocBlockElement>
) : DocBlockElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitQuote(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {
        content.forEach { it.accept(visitor, data) }
    }

}