package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.processing.DocVisitor

@Adat
class DocDocument(
    override val style: DocStyleId,
    val blocks: List<DocBlockElement>,
    val styles : List<DocStyle>
) : DocBlockElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitDocument(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {
        blocks.forEach { it.accept(visitor, data) }
    }

}
