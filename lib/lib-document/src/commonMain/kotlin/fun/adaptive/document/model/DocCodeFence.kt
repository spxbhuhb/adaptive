package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.visitor.DocVisitor

@Adat
class DocCodeFence(
    override val style: DocStyleId,
    val code : String,
    val language : String? = null
) : DocBlockElement() {

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitCodeFence(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {

    }

}