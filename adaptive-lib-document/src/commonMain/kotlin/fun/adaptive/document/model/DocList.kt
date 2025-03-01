package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocList(
    override val style: DocStyleId,
    override val children: List<DocBlockElement>,
    val bullet : Boolean,
    val level : Int
) : DocBlockElement()