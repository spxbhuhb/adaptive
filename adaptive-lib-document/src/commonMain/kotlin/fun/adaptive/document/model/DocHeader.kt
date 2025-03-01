package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocHeader(
    override val style: DocStyleId,
    override val children: List<DocInlineElement>,
    val level : Int
) : DocBlockElement()