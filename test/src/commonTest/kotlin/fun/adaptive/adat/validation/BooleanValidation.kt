package `fun`.adaptive.adat.validation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Adat
class BooleanTest(
    val someBoolean : Boolean
) {
    override fun descriptor() {
        properties {
            someBoolean value true default false
        }
    }
}

class BooleanValidation {

    @Test
    fun invalid() {
        val t = BooleanTest(false)
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertFalse(result.isValid)
        assertEquals(1, result.failedConstraints.size)

        val fc = result.failedConstraints.first()

        assertEquals("BooleanValue", fc.descriptorMetadata.name)
    }

    @Test
    fun valid() {
        val t = BooleanTest(true)
        t.validate()

        val result = t.adatContext!!.validationResult!!

        assertTrue(result.isValid)
        assertEquals(0, result.failedConstraints.size)
    }
}