package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocText(
    override val style: DocStyleId,
    val text: String
) : DocInlineElement()