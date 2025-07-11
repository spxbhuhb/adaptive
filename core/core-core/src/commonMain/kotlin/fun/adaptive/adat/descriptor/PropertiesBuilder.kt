package `fun`.adaptive.adat.descriptor

class PropertiesBuilder {

    // --------------------------------------------------------------------------------
    // Boolean
    // --------------------------------------------------------------------------------

    @AdatDescriptorName("BooleanValue")
    infix fun Boolean.value(expected: Boolean): Boolean = this

    @AdatDescriptorName("BooleanDefault")
    infix fun Boolean.default(value: Boolean): Boolean = this

    // --------------------------------------------------------------------------------
    // Double
    // --------------------------------------------------------------------------------

    @AdatDescriptorName("DoubleMinimum")
    infix fun Double.minimum(minimum: Double): Double = this

    @AdatDescriptorName("DoubleMaximum")
    infix fun Double.maximum(maximum: Double): Double = this

    @AdatDescriptorName("DoubleDefault")
    infix fun Double.default(value: Double): Double = this

    // --------------------------------------------------------------------------------
    // Int
    // --------------------------------------------------------------------------------

    @AdatDescriptorName("IntMinimum")
    infix fun Int.minimum(minimum: Int): Int = this

    @AdatDescriptorName("IntMaximum")
    infix fun Int.maximum(maximum: Int): Int = this

    @AdatDescriptorName("IntDefault")
    infix fun Int.default(value: Int): Int = this

    // --------------------------------------------------------------------------------
    // Long
    // --------------------------------------------------------------------------------

    @AdatDescriptorName("LongMinimum")
    infix fun Long.minimum(minimum: Long): Long = this

    @AdatDescriptorName("LongMaximum")
    infix fun Long.maximum(maximum: Long): Long = this

    @AdatDescriptorName("LongDefault")
    infix fun Long.default(value: Long): Long = this

    // --------------------------------------------------------------------------------
    // String
    // --------------------------------------------------------------------------------

    @AdatDescriptorName("StringDefault")
    infix fun String?.default(value: String): String? = this

    @AdatDescriptorName("StringBlank")
    infix fun String?.blank(allowBlank: Boolean): String? = this

    @AdatDescriptorName("StringSecret")
    infix fun String?.secret(isSecret: Boolean): String? = this

    @AdatDescriptorName("StringMinLength")
    infix fun String?.minLength(minLength: Int): String? = this

    @AdatDescriptorName("StringMaxLength")
    infix fun String?.maxLength(maxLength: Int): String? = this

    @AdatDescriptorName("StringPattern")
    infix fun String?.pattern(pattern: String): String? = this

    // --------------------------------------------------------------------------------
    // General
    // --------------------------------------------------------------------------------

    @AdatDescriptorName("Hidden")
    infix fun <A> A.hidden(value: Boolean): A = this

    @AdatDescriptorName("Readonly")
    infix fun <A> A.readonly(value: Boolean): A = this

    @AdatDescriptorName("UseToString")
    infix fun <A> A.useToString(value: Boolean): A = this

}