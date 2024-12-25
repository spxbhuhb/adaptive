package `fun`.adaptive.kotlin.writer

import `fun`.adaptive.kotlin.writer.model.KwElement
import `fun`.adaptive.kotlin.writer.model.KwFile
import `fun`.adaptive.kotlin.writer.model.KwModule
import `fun`.adaptive.kotlin.writer.model.KwSymbol
import `fun`.adaptive.utility.isSpace

class KotlinWriter {

    val modules = mutableListOf<KwModule>()

    private val out = StringBuilder()

    private var indent = ""

    private var newLine = false

    private lateinit var file : KwFile

    operator fun plusAssign(kwElement: KwElement) {
        addIndentOrSpace()
        out.append(kwElement.toKotlin(this))
    }

    fun add(kwElement: KwElement) : KotlinWriter {
        addIndentOrSpace()
        kwElement.toKotlin(this)
        return this
    }

    operator fun plusAssign(s : String) {
        if (s != ".") {
            addIndentOrSpace()
        }
        out.append(s)
    }

    fun add(s : String) : KotlinWriter {
        if (s != ".") {
            addIndentOrSpace()
        }
        out.append(s)
        return this
    }

    fun name(name : String) : KotlinWriter =
        add(name)

    operator fun plusAssign(enum : Enum<*>) {
        addIndentOrSpace()
        out.append(enum.name.lowercase())
    }

    fun symbol(symbol : KwSymbol) : KotlinWriter {
        addIndentOrSpace()

        val import = file.imports.find { it.name == symbol.name || it.alias == symbol.name }
        if (import != null) {
            if (import.alias != null) {
                out.append(import.alias)
            } else {
                out.append(symbol.name.substringAfterLast('.'))
            }
        } else {
            out.append(symbol.name)
        }
        return this
    }

    fun newLine() : KotlinWriter {
        out.appendLine()
        newLine = true
        return this
    }

    fun openBlock()  : KotlinWriter{
        addIndentOrSpace()
        out.append("{")
        return this
    }

    fun closeBlock()  : KotlinWriter{
        addIndentOrSpace()
        out.append("}")
        return this
    }

    fun noArgCall() : KotlinWriter {
        out.append("()")
        return this
    }

    fun openParenthesis()  : KotlinWriter{
        out.append("(")
        return this
    }

    fun closeParenthesis()  : KotlinWriter{
        out.append(")")
        return this
    }

    fun openString() : KotlinWriter {
        out.append('"')
        return this
    }

    fun escapedString(s : String) : KotlinWriter {
        out.append(s.replace("\"", "\\\""))
        return this
    }

    fun closeString() : KotlinWriter {
        out.append('"')
        return this
    }

    fun withIndent(block: KotlinWriter.() -> Unit)  : KotlinWriter{
        indent += "    "
        block()
        indent = indent.removeSuffix("    ")
        return this
    }

    fun lines(elements: Iterable<KwElement>) : KotlinWriter {
        elements.forEach {
            it.toKotlin(this)
            newLine()
        }
        return this
    }

    fun declarations(elements: Iterable<KwElement>) : KotlinWriter {
        elements.forEach {
            it.toKotlin(this)
            newLine()
            newLine()
        }
        return this
    }

    fun join(elements: Iterable<KwElement>, separator: String = ", ") : KotlinWriter {
        elements.forEachIndexed { index, element ->
            if (index > 0) {
                out.append(separator)
            }
            element.toKotlin(this)
        }
        return this
    }

    private fun addIndentOrSpace() {
        val last = out.lastOrNull()
        when {
            newLine -> {
                out.append(indent)
                newLine = false
            }

            last == '.' -> Unit
            last == '(' -> Unit

            last?.isSpace() == false -> {
                out.append(' ')
            }
        }
    }


    fun render() {
        modules.forEach {
            for (file in it.files) {
                this.file = file
                file.toKotlin(this)
                file.renderedSource = out.toString()
                out.clear()
            }
        }
    }

    override fun toString(): String {
        throw IllegalStateException("KotlinWriter is not intended to be used as a string")
    }
}