package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocParagraph(
    override val style: DocStyleId,
    override val children: List<DocInlineElement>,
    val standalone : Boolean
) : DocBlockElement()