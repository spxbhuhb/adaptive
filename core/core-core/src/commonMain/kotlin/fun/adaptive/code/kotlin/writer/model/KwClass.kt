package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

class KwClass(
    val name: String,
    val visibility: KwVisibility,
    val modality: KwModality,
    val kind: KwClassKind
) : KwDeclarationBase(), KwDeclarationContainer {

    override val declarations = mutableListOf<KwDeclaration>()

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {

        if (visibility != KwVisibility.PUBLIC) writer += visibility
        if (modality != KwModality.FINAL) writer += modality

        writer += kind.token
        writer += name

        writer
            .openBlock()
            .newLine()
            .newLine()
            .withIndent {
                declarations(declarations)
            }
            .closeBlock()

        return writer
    }

}