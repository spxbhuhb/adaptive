package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocDocument(
    override val style: DocStyleId,
    override val children: List<DocBlockElement>,
    val styles : List<DocStyle>
) : DocBlockElement()
