package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.Adat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class DeepCopyTest {

    @Test
    fun im1() {
        val original = I1(12)

        assertEquals(original, original.deepCopy())
    }

    @Test
    fun im2() {
        val original = I2(12, I1(23))
        val copy = original.deepCopy()

        assertEquals(original, copy)
        assertSame(original.i1, original.i1) // immutable, so should use the same instance
    }

    @Test
    fun m1() {
        val original = M1(12)
        assertEquals(original, original.deepCopy())
    }

    @Test
    fun m2() {
        val original = M2(12, M1(23), I1(34))
        val copy = original.deepCopy()

        assertEquals(original, copy)
        assertNotSame(original.m1, copy.m1)
        assertSame(original.i1, copy.i1)
    }
}

@Adat
class I1(
    val v: Int
)

@Adat
class I2(
    val v: Int,
    val i1: I1
)

@Adat
class M1(
    var v: Int
)

@Adat
class M2(
    var v: Int,
    var m1: M1,
    var i1: I1
)