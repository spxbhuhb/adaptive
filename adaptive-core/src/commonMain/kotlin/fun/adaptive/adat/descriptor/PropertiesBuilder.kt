package `fun`.adaptive.adat.descriptor

class PropertiesBuilder {

    infix fun Boolean.value(expected: Boolean): Boolean = this
    infix fun Boolean.default(value: Boolean): Boolean = this

    infix fun Int.minimum(minimum: Long): Int = this
    infix fun Int.maximum(maximum: Long): Int = this
    infix fun Int.default(value: Int): Int = this

    infix fun String.default(value: String): String = this
    infix fun String.blank(allowBlank: Boolean): String = this
    infix fun String.secret(isSecret: Boolean): String = this
    infix fun String.minLength(minLength: Int): String = this
    infix fun String.maxLength(maxLength: Int): String = this
    infix fun String.pattern(pattern: String): String = this

}