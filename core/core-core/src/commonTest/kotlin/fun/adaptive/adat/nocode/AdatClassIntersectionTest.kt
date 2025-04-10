package `fun`.adaptive.adat.nocode

import kotlin.test.Test
import kotlin.test.assertEquals

class AdatClassIntersectionTest {

    @Test
    fun one() {
        val t1 = TestClass(12, "a")

        val optional = TestClass.optional()
        val values = arrayOfNulls<Pair<Int, Any?>>(2)
        val intersection = AdatClassIntersection(optional, listOf(t1)) { i, v -> values[i] = i to v  }

        assertEquals(12, intersection.getValue(0))
        assertEquals("a", intersection.getValue(1))

        intersection.setValue(0, 123)
        intersection.setValue(1, "Hello")

        assertEquals(0 to 123, values[0])
        assertEquals(1 to "Hello", values[1])
    }

    @Test
    fun three() {
        val t1 = TestClass(12, "a")
        val t2 = TestClass(23, "a")
        val t3 = TestClass(45, "a")

        val optional = TestClass.optional()
        val values = arrayOfNulls<Any?>(2)
        val intersection = AdatClassIntersection(optional, listOf(t1, t2, t3)) { i, v -> values[i] = i to v }

        assertEquals(null, intersection.getValue(0))
        assertEquals("a", intersection.getValue(1))

        intersection.setValue(0, 123)
        intersection.setValue(1, "Hello")

        assertEquals(0 to 123, values[0])
        assertEquals(1 to "Hello", values[1])
    }

}