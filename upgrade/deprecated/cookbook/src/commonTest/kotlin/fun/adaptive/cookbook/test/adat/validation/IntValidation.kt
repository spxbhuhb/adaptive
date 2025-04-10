package `fun`.adaptive.cookbook.test.adat.validation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.api.validate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Adat
class IntTest(
    val someInt : Int
) {
    override fun descriptor() {
        properties {
            someInt minimum 23 maximum 100 default 12
        }
    }
}

class IntValidation {

    @Test
    fun minimum() {
        val t = IntTest(1)
        val result = t.validate()

        assertFalse(result.isValid)
        assertEquals(1, result.failedConstraints.size)

        val fc = result.failedConstraints.first()

        assertEquals("IntMinimum", fc.descriptorMetadata.name)
    }


    @Test
    fun maximum() {
        val t = IntTest(102)
        val result = t.validate()

        assertFalse(result.isValid)
        assertEquals(1, result.failedConstraints.size)

        val fc = result.failedConstraints.first()

        assertEquals("IntMaximum", fc.descriptorMetadata.name)
    }

    @Test
    fun valid() {
        val t = IntTest(34)
        val result = t.validate()

        assertTrue(result.isValid)
        assertEquals(0, result.failedConstraints.size)
    }
}