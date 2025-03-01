package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocLink(
    override val style: DocStyleId,
    val text: String,
    val url: String
) : DocInlineElement()