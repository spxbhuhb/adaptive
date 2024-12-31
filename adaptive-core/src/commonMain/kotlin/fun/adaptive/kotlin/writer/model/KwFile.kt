package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwFile(
    val module: KwModule,
    var name: String,
    val packageName: String,
) : KwElement, KwDeclarationContainer {

    val imports = mutableListOf<KwImport>()

    var renderedSource = ""

    override val declarations = mutableListOf<KwDeclaration>()

    /**
     * File generation is split into two so we can optimize imports.
     */
    fun headerToKotlin(writer: KotlinWriter, symbols: Set<KwSymbol>) {
        val usedImports = imports.filter { it.symbol in symbols }

        if (packageName.isNotEmpty()) {
            writer
                .add("package")
                .symbol(KwSymbol(packageName))
                .newLine()
                .newLine()
        }

        if (usedImports.isNotEmpty()) {
            writer
                .lines(imports.filter { it.symbol in symbols })
                .newLine()
        }
    }

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        writer.declarations(declarations)

}