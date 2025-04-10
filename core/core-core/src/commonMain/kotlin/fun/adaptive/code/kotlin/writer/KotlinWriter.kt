package `fun`.adaptive.code.kotlin.writer

import `fun`.adaptive.code.kotlin.writer.model.KwElement
import `fun`.adaptive.code.kotlin.writer.model.KwFile
import `fun`.adaptive.code.kotlin.writer.model.KwModule
import `fun`.adaptive.code.kotlin.writer.model.KwSymbol
import `fun`.adaptive.utility.isSpace

class KotlinWriter {

    val modules = mutableListOf<KwModule>()

    private val out = StringBuilder()

    private var indent = ""

    private var newLine = false

    private lateinit var file: KwFile

    private val symbols = mutableSetOf<KwSymbol>()

    operator fun plusAssign(kwElement: KwElement) {
        addIndentOrSpace()
        out.append(kwElement.toKotlin(this))
    }

    fun add(kwElement: KwElement): KotlinWriter {
        addIndentOrSpace()
        kwElement.toKotlin(this)
        return this
    }

    operator fun plusAssign(s: String) {
        if (s != ".") {
            addIndentOrSpace()
        }
        out.append(s)
    }

    fun add(s: String): KotlinWriter {
        if (s != ".") {
            addIndentOrSpace()
        }
        out.append(s)
        return this
    }

    fun name(name: String): KotlinWriter =
        add(name)

    operator fun plusAssign(enum: Enum<*>) {
        addIndentOrSpace()
        out.append(enum.name.lowercase())
    }

    fun symbol(symbol: KwSymbol): KotlinWriter {
        addIndentOrSpace()

        symbols += symbol

        val import = file.imports.find { it.symbol == symbol || it.alias == symbol.name } // FIXME I'm not sure matching symbols with names is correct

        val escapedName : String
        if (symbol.name.startsWith('`') && symbol.name.endsWith('`')) {
            escapedName = symbol.name
        } else {
            escapedName = symbol.name.split('.').joinToString(".") { it.kotlinEscape() }
        }

        if (import != null) {
            if (import.alias != null) {
                out.append(import.alias)
            } else {
                out.append(escapedName.substringAfterLast('.'))
            }
        } else {
            out.append(escapedName)
        }

        return this
    }

    fun newLine(): KotlinWriter {
        out.appendLine()
        newLine = true
        return this
    }

    fun openBlock(): KotlinWriter {
        addIndentOrSpace()
        out.append("{")
        return this
    }

    fun closeBlock(): KotlinWriter {
        addIndentOrSpace()
        out.append("}")
        return this
    }

    fun noArgCall(): KotlinWriter {
        out.append("()")
        return this
    }

    fun openParenthesis(): KotlinWriter {
        out.append("(")
        return this
    }

    fun closeParenthesis(): KotlinWriter {
        addIndent()
        out.append(")")
        return this
    }

    fun openString(): KotlinWriter {
        out.append('"')
        return this
    }

    fun escapedString(s: String): KotlinWriter {
        out.append(s.replace("\"", "\\\""))
        return this
    }

    fun closeString(): KotlinWriter {
        out.append('"')
        return this
    }

    fun withIndent(block: KotlinWriter.() -> Unit): KotlinWriter {
        indent += "    "
        block()
        indent = indent.removeSuffix("    ")
        return this
    }

    fun lines(elements: Iterable<KwElement>): KotlinWriter {
        elements.forEach {
            it.toKotlin(this)
            newLine()
        }
        return this
    }

    fun declarations(elements: Iterable<KwElement>): KotlinWriter {
        elements.forEach {
            it.toKotlin(this)
            newLine()
            newLine()
        }
        return this
    }

    fun join(elements: Iterable<KwElement>, separator: String = ",", multiLine : Boolean = false): KotlinWriter {
        elements.forEachIndexed { index, element ->
            if (index > 0) {
                out.append(separator)
                if (multiLine) newLine()
            }
            element.toKotlin(this)
        }
        return this
    }

    private fun addIndent() {
        if (newLine) {
            out.append(indent)
            newLine = false
        }
    }
    
    /**
     * Escapes a word if it is not a valid Kotlin identifier.
     * If the word is already a valid Kotlin identifier, the original word is returned.
     */
    private fun String.kotlinEscape(): String {
        if (this.startsWith('`') && this.endsWith('`')) return this
        if (this.isNotEmpty() && this.first().isLetterOrUnderscore() && this.all { it.isLetterOrDigitOrUnderscore() }) {
            if (this in reservedWords) return "`$this`"
            return this
        }
        return "`$this`"
    }
    
    private fun Char.isLetterOrUnderscore(): Boolean = this.isLetter() || this == '_'
    
    private fun Char.isLetterOrDigitOrUnderscore(): Boolean = this.isLetterOrDigit() || this == '_'

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

    fun render(): KotlinWriter {
        modules.forEach {
            for (file in it.files) {
                this.file = file

                file.toKotlin(this)
                val declarations = out.toString()
                out.clear()

                file.headerToKotlin(this, symbols)
                val header = out.toString()
                out.clear()
                symbols.clear()

                file.renderedSource = header + declarations.trimEnd()
            }
        }

        return this
    }

    override fun toString(): String {
        throw IllegalStateException("KotlinWriter is not intended to be used as a string")
    }

    companion object {
        val reservedWords = setOf(
            "as", "break", "class", "continue", "do", "else", "false", "for", "fun",
            "if", "in", "interface", "is", "null", "object", "package", "return",
            "super", "this", "throw", "true", "try", "typealias", "typeof", "val",
            "var", "when", "while"
        )
    }
}