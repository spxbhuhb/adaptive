package `fun`.adaptive.resource

import `fun`.adaptive.kotlin.writer.model.KwSymbol
import `fun`.adaptive.resource.codegen.kotlin.KwResourceSymbols

enum class ResourceTypeQualifier(
    val objectSymbol: KwSymbol,
    val fileSymbol: KwSymbol,
    val setSymbol: KwSymbol,
    val isUnstructured : Boolean = true
) : Qualifier {

    File(KwResourceSymbols.files, KwResourceSymbols.fileResource, KwResourceSymbols.fileResourceSet),
    Font(KwResourceSymbols.fonts, KwResourceSymbols.fontResource, KwResourceSymbols.fontResourceSet),
    Graphics(KwResourceSymbols.graphics, KwResourceSymbols.graphicsResource, KwResourceSymbols.graphicsResourceSet),
    Image(KwResourceSymbols.images, KwResourceSymbols.imageResource, KwResourceSymbols.imageResourceSet),
    Strings(KwResourceSymbols.strings, KwResourceSymbols.fileResource, KwResourceSymbols.stringStoreResourceSet, false);

    companion object {
        fun parse(value: String): Qualifier? = when (value) {
            "file" -> File
            "font" -> Font
            "graphics" -> Graphics
            "image" -> Image
            "strings" -> Strings
            else -> null
        }
    }
}