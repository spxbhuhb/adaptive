package `fun`.adaptive.document.model

abstract class DocBlockElement : DocElement() {
    abstract val children: List<DocElement>
}