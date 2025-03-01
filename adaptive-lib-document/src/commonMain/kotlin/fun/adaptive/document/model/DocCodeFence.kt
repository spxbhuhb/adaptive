package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocCodeFence(
    override val style: DocStyleId,
    val code : String,
    val language : String? = null
) : DocBlockElement() {

    override val children
        get() = emptyList<DocElement>()

}