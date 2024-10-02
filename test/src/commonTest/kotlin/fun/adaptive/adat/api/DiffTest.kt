package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.Adat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class DiffTest {

    @Test
    fun m2() {
        val a1 = M2(12, M1(23), I1(34))
        val a2 = M2(12, M1(23), I1(45))

        val diff = a1.diff(a2)
        assertEquals(1, diff.size)
    }
}