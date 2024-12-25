package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.ANONYMOUS
import `fun`.adaptive.kotlin.writer.KotlinWriter

class KwFunction(
    var name: String,
) : KwDeclarationBase(), KwExpression {

    var visibility: KwVisibility = KwVisibility.PUBLIC
    var modality: KwModality = KwModality.FINAL

    val isAnonymous
        get() = name == ANONYMOUS

    val isPropertyAccessor
        get() = name.startsWith("<get-") || name.startsWith("<set-")

    val isPropertyDelegation
        get() = name.startsWith("<delegate-")

    var valueParameters = mutableListOf<KwElement>() // TODO this should be a list of KwValueParameter

    var body: KwBody? = null

    override fun toKotlin(writer: KotlinWriter): KotlinWriter {

        when {
            isPropertyAccessor -> optimizedBody(writer)
            isPropertyDelegation -> optimizedBody(writer)
            isAnonymous -> optimizedBody(writer)
            else -> named(writer)
        }

        return writer
    }

    fun named(writer: KotlinWriter) {
        if (visibility != KwVisibility.PUBLIC) writer += visibility
        if (modality != KwModality.FINAL) writer += modality

        writer
            .add("fun")
            .name(name)
            .openParenthesis()
            .join(valueParameters)
            .closeParenthesis()

        optimizedBody(writer)
    }

    fun optimizedBody(writer: KotlinWriter) {
        val safeBody = body

        if (safeBody == null) return

        if (safeBody is KwExpressionBody || (safeBody is KwBlockBody && safeBody.statements.size == 1)) {

            val statement = safeBody.statements.first()

            when {
                isPropertyDelegation -> writer.add(statement)
                isPropertyAccessor -> writer.add("=").add(statement)
                isAnonymous -> writer.openBlock().add(statement).closeBlock()
                else -> writer.add("=").newLine().withIndent { add(statement) }
            }

            return
        }

        if (isPropertyAccessor) {
            writer.add("by")
        }

        safeBody.toKotlin(writer)
    }

}