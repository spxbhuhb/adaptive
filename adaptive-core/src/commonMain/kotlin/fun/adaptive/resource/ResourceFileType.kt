package `fun`.adaptive.resource

import `fun`.adaptive.kotlin.writer.model.KwSymbol
import `fun`.adaptive.resource.generation.KwResourceSymbols

enum class ResourceFileType(
    val objectSymbol : KwSymbol,
    val fileSymbol : KwSymbol,
    val setSymbol : KwSymbol,
) {
    File(KwResourceSymbols.files, KwResourceSymbols.fileResource, KwResourceSymbols.fileResourceSet),
    Font(KwResourceSymbols.fonts, KwResourceSymbols.fontResource, KwResourceSymbols.fontResourceSet),
    Graphics(KwResourceSymbols.graphics, KwResourceSymbols.graphicsResource, KwResourceSymbols.graphicsResourceSet),
    Image(KwResourceSymbols.images, KwResourceSymbols.imageResource, KwResourceSymbols.imageResourceSet),
    StringStore(KwResourceSymbols.strings, KwResourceSymbols.fileResource, KwResourceSymbols.stringStoreResourceSet)
}