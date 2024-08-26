/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AdatTest {

    val sl = setOf(listOf(1,2),listOf(3,4))
    val t = TestClass(12, false, sl)

    @Test
    fun basic() {
        val sl = setOf(listOf(1,2),listOf(3,4))

        val t1 = TestClass(12, false, sl)

        assertEquals(t1, t1)
        assertEquals(t1, t1.copy())
        assertNotEquals(t1, TestClass(12, true, sl))

        assertEquals(t1, TestClass.fromJson(t1.toJson()))
        assertEquals(t1, TestClass.fromProto(t1.toProto()))
    }

    @Test
    fun diffTestSame() {
        val same = TestClass(12, false, sl)

        assertTrue(t.diff(t).isEmpty())
        assertTrue(t.diff(same).isEmpty())
    }

    @Test
    fun diffTestValue() {
        val o = TestClass(11, true, sl)

        val result = t.diff(o)

        assertEquals(2, result.size)

        assertEquals(AdatDiffKind.ValueDiff, result[0].kind)
        assertEquals("someInt", result[0].path)
        assertEquals(0, result[0].index)

        assertEquals(AdatDiffKind.ValueDiff, result[1].kind)
        assertEquals("someBoolean", result[1].path)
        assertEquals(1, result[1].index)
    }

    @Test
    fun diffTestSignature() {
        val o = TestClassSignatureDiff(12f, false, sl)

        val result = t.diff(o)

        assertEquals(1, result.size)
        assertEquals(AdatDiffKind.SignatureDiff, result.first().kind)
        assertEquals("someInt", result.first().path)
        assertEquals(0, result.first().index)
    }

    @Test
    fun diffTestIndex() {
        val o = TestClassIndexDiff(false, 12, sl)

        val result = t.diff(o)

        assertEquals(2, result.size)

        assertEquals(AdatDiffKind.IndexDiff, result[0].kind)
        assertEquals("someInt", result[0].path)
        assertEquals(null, result[0].index)

        assertEquals(AdatDiffKind.IndexDiff, result[1].kind)
        assertEquals("someBoolean", result[1].path)
        assertEquals(null, result[1].index)
    }

    @Test
    fun diffTestMissing() {
        val o = TestClassMissingDiff(12, false, sl)

        val result = t.diff(o)

        assertEquals(2, result.size)

        assertEquals(AdatDiffKind.MissingFromOther, result[0].kind)
        assertEquals("someBoolean", result[0].path)
        assertEquals(null, result[0].index)

        assertEquals(AdatDiffKind.MissingFromThis, result[1].kind)
        assertEquals("someOtherField", result[1].path)
        assertEquals(null, result[1].index)
    }
}