package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.visitor.DocVisitor

@Adat
class DocList(
    override val style: DocStyleId,
    val items: List<DocListItem>,
    val standalone: Boolean
) : DocBlockElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitList(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {
        items.forEach { it.accept(visitor, data) }
    }

}