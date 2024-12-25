package `fun`.adaptive.kotlin.writer.model

enum class KwClassKind(
    val token : String
) {
    CLASS("class"),
    OBJECT("object"),
    INTERFACE("interface"),
    ENUM("enum class"),
    ANNOTATION_CLASS("annotation")
}