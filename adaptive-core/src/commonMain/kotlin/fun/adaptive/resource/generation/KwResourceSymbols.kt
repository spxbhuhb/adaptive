package `fun`.adaptive.resource.generation

import `fun`.adaptive.kotlin.writer.kwSymbol

object KwResourceSymbols {
    val files = kwSymbol("`fun`.adaptive.resource.file.Files")
    val fileResource = kwSymbol("`fun`.adaptive.resource.file.FileResource")
    val fileResourceSet = kwSymbol("`fun`.adaptive.resource.file.FileResourceSet")

    val strings = kwSymbol("`fun`.adaptive.resource.string.Strings")
    val stringStoreResourceSet = kwSymbol("`fun`.adaptive.resource.string.StringStoreResourceSet")

    val languageQualifier = kwSymbol("`fun`.adaptive.resource.LanguageQualifier")
    val regionQualifier = kwSymbol("`fun`.adaptive.resource.RegionQualifier")
}