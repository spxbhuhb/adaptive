package `fun`.adaptive.document.model

import `fun`.adaptive.adat.Adat

@Adat
class DocRule : DocBlockElement() {

    override val style: DocStyleId
        get() = -1

    override val children: List<DocElement>
        get() = emptyList()

}