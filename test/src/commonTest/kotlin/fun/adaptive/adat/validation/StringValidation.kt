package `fun`.adaptive.adat.validation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.api.properties
import `fun`.adaptive.adat.descriptor.kotlin.string.StringBlank
import `fun`.adaptive.adat.descriptor.kotlin.string.StringMaxLength
import `fun`.adaptive.adat.descriptor.kotlin.string.StringMinLength
import `fun`.adaptive.adat.descriptor.kotlin.string.StringPattern
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@Adat
class StringTest(
    val someString : String
) {
    override fun descriptor() {
        properties {
            someString minLength 2 maxLength 4 default "1234" blank false pattern "[0-9]*" secret true
        }
    }
}

class StringValidation {

    @Test
    fun minLength() {
        val t = StringTest("1")
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertFalse(result.isValid)
        assertEquals(1, result.failedConstraints.size)

        val fc = result.failedConstraints.first()

        assertIs<StringMinLength>(fc)
    }


    @Test
    fun maxLength() {
        val t = StringTest("12345")
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertFalse(result.isValid)
        assertEquals(1, result.failedConstraints.size)

        val fc = result.failedConstraints.first()

        assertIs<StringMaxLength>(fc)
    }

    @Test
    fun blank() {
        val t = StringTest("  ")
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertFalse(result.isValid)
        assertEquals(2, result.failedConstraints.size)

        result.failedConstraints.first { it is StringBlank }
    }

    @Test
    fun pattern() {
        val t = StringTest("abcd")
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertFalse(result.isValid)
        assertEquals(1, result.failedConstraints.size)

        val fc = result.failedConstraints.first()

        assertIs<StringPattern>(fc)
    }

    @Test
    fun valid() {
        val t = StringTest("1234")
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertTrue(result.isValid)
        assertEquals(0, result.failedConstraints.size)
    }
}