/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.utility

import kotlin.test.Test
import kotlin.test.assertEquals

class NumberTest {

    @Test
    fun testDoubleDecimals() {
        assertEquals("0.0", format(0.0))
        assertEquals("0.0", format(- 0.0))
        assertEquals("0", format(0.0, 0))
        assertEquals("0", format(- 0.0, 0))
        assertEquals("0.00", format(0.0, 2))

        assertEquals("1.2", format(1.2))
        assertEquals("1", format(1.2, 0))
        assertEquals("2", format(1.5, 0))

        assertEquals("12", format(12.0, 0))
        assertEquals("12.0", format(12.0, 1))
        assertEquals("12.00", format(12.0, 2))
        assertEquals("12.000", format(12.0, 3))
        assertEquals("12.0000", format(12.0, 4))

        assertEquals("12", format(12.3456, 0))
        assertEquals("12.3", format(12.3456, 1))
        assertEquals("12.35", format(12.3456, 2))
        assertEquals("12.346", format(12.3456, 3))
        assertEquals("12.3456", format(12.3456, 4))
        assertEquals("12.34560", format(12.3456, 5))

        assertEquals("-12", format(- 12.0, 0))
        assertEquals("-12.0", format(- 12.0, 1))
        assertEquals("-12.00", format(- 12.0, 2))
        assertEquals("-12.000", format(- 12.0, 3))
        assertEquals("-12.0000", format(- 12.0, 4))

        assertEquals("-12", format(- 12.3456, 0))
        assertEquals("-12.3", format(- 12.3456, 1))
        assertEquals("-12.35", format(- 12.3456, 2))
        assertEquals("-12.346", format(- 12.3456, 3))
        assertEquals("-12.3456", format(- 12.3456, 4))
        assertEquals("-12.34560", format(- 12.3456, 5))

        assertEquals("0", format(0.0, 0))
        assertEquals("0.0", format(0.0, 1))
        assertEquals("0.00", format(0.0, 2))
        assertEquals("0.000", format(0.0, 3))
        assertEquals("0.0000", format(0.0, 4))

        assertEquals("0", format(- 0.3456, 0))
        assertEquals("-0.3", format(- 0.3456, 1))
        assertEquals("-0.35", format(- 0.3456, 2))
        assertEquals("-0.346", format(- 0.3456, 3))
        assertEquals("-0.3456", format(- 0.3456, 4))
        assertEquals("-0.34560", format(- 0.3456, 5))

        assertEquals("0.09", format(0.09, 2))
        assertEquals("1.00", format(0.999, 2))
        assertEquals("0.99", format(0.994, 2))
        assertEquals("1.00", format(0.995, 2))
        assertEquals("-1.00", format(- 0.999, 2))

        assertEquals("-Inf", format(Double.NEGATIVE_INFINITY, 0))
        assertEquals("NaN", format(Double.NaN, 0))
        assertEquals("+Inf", format(Double.POSITIVE_INFINITY, 0))

        assertEquals("-Inf", format(Double.NEGATIVE_INFINITY, 1))
        assertEquals("NaN", format(Double.NaN, 1))
        assertEquals("+Inf", format(Double.POSITIVE_INFINITY, 1))
    }

}