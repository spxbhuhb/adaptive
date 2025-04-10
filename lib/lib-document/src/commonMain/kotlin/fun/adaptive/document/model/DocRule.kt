package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.document.processing.DocVisitor

@Adat
class DocRule : DocBlockElement() {

    override val style: DocStyleId
        get() = -1

    override fun <R, D> accept(visitor: DocVisitor<R, D>, data: D): R =
        visitor.visitRule(this, data)

    override fun <D> acceptChildren(visitor: DocVisitor<Unit, D>, data: D) {

    }

}