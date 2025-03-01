package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocQuote(
    override val style: DocStyleId,
    override val children: List<DocBlockElement>
) : DocBlockElement()