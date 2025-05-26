package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.visitor.DocVisitor

@Adat
class DocListItem(
    override val style: DocStyleId,
    val content: DocBlockElement,
    val subList: DocList?,
    val path: List<Int>,
    val bullet: Boolean
) : DocBlockElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitListItem(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {
        content.accept(visitor, data)
        subList?.accept(visitor, data)
    }

}