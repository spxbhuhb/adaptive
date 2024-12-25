package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwProperty(
    var name: String
) : KwDeclarationBase() {

    var modality: KwModality = KwModality.FINAL
    var visibility: KwVisibility = KwVisibility.PUBLIC

    var isVal = true
    var receiver: KwSymbol? = null
    var type: KwSymbol? = null
    var initializer : KwExpression? = null
    var getter: KwFunction? = null

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {

        if (modality != KwModality.FINAL) writer += modality
        if (visibility != KwVisibility.PUBLIC) writer += visibility

        if (isVal) writer += "val" else writer += "var"

        writer.name(name)

        type?.let { writer.add(":").symbol(it) }

        val safeInitializer = initializer

        when (safeInitializer) {
            is KwDelegation -> writer.add("by").add(safeInitializer)
            is KwExpression -> writer.add("=").add(safeInitializer)
        }

        val safeGetter = getter
        if (safeGetter != null) {
            writer
                .newLine()
                .withIndent {
                    add("get()")
                    add(safeGetter)
                }
        }

        return writer
    }

}
