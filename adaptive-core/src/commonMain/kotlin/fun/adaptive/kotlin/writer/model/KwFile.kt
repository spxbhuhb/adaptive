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

    override fun toKotlin(writer: KotlinWriter): KotlinWriter =
        writer
            .add("package")
            .add(packageName)
            .newLine()
            .newLine()
            .lines(imports)
            .newLine()
            .declarations(declarations)

}