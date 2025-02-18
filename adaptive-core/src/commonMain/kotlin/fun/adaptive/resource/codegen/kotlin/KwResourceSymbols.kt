package `fun`.adaptive.resource.codegen.kotlin

import `fun`.adaptive.code.kotlin.writer.kwSymbol

object KwResourceSymbols {
    val files = kwSymbol("`fun`.adaptive.resource.file.Files")
    val fileResource = kwSymbol("`fun`.adaptive.resource.file.FileResource")
    val fileResourceSet = kwSymbol("`fun`.adaptive.resource.file.FileResourceSet")

    val fonts = kwSymbol("`fun`.adaptive.resource.font.Fonts")
    val fontResource = kwSymbol("`fun`.adaptive.resource.font.FontResource")
    val fontResourceSet = kwSymbol("`fun`.adaptive.resource.font.FontResourceSet")

    val graphics = kwSymbol("`fun`.adaptive.resource.graphics.Graphics")
    val graphicsResource = kwSymbol("`fun`.adaptive.resource.graphics.GraphicsResource")
    val graphicsResourceSet = kwSymbol("`fun`.adaptive.resource.graphics.GraphicsResourceSet")

    val images = kwSymbol("`fun`.adaptive.resource.image.Images")
    val imageResource = kwSymbol("`fun`.adaptive.resource.image.ImageResource")
    val imageResourceSet = kwSymbol("`fun`.adaptive.resource.image.ImageResourceSet")

    val strings = kwSymbol("`fun`.adaptive.resource.string.Strings")
    val stringStoreResourceSet = kwSymbol("`fun`.adaptive.resource.string.StringStoreResourceSet")

    val languageQualifier = kwSymbol("`fun`.adaptive.resource.LanguageQualifier")
    val regionQualifier = kwSymbol("`fun`.adaptive.resource.RegionQualifier")
    val themeQualifier = kwSymbol("`fun`.adaptive.resource.ThemeQualifier")
    val densityQualifier = kwSymbol("`fun`.adaptive.resource.DensityQualifier")
}