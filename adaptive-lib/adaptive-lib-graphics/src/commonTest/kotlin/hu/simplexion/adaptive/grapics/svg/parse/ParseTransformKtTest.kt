/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.parse

import hu.simplexion.adaptive.grapics.svg.instruction.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParseTransformKtTest {

    @Test
    fun testParseTransform() {
        val source =
            listOf(
                "translate(-10,-20) ",
                "translate(15) ",
                "scale(2) ",
                "scale(3 4) ",
                "rotate(45) ",
                "rotate(45,46 47) ",
                "matrix(0 1 2 3 4 5) ",
                "skewX(12) ",
                "skewY(13) "
            ).joinToString(" ")

        val result = parseTransform(source)

        assertNotNull(result)
        assertEquals(9, result.size)

        var index = 0
        assertEquals(Translate(- 10.0, - 20.0), result[index ++])
        assertEquals(Translate(15.0, 0.0), result[index ++])

        assertEquals(Scale(2.0, 2.0), result[index ++])
        assertEquals(Scale(3.0, 4.0), result[index ++])

        assertEquals(Rotate(45.0, 0.0, 0.0), result[index ++])
        assertEquals(Rotate(45.0, 46.0, 47.0), result[index ++])

        assertEquals(Matrix(0.0, 1.0, 2.0, 3.0, 4.0, 5.0), result[index++])

        assertEquals(SkewX(12.0), result[index ++])
        assertEquals(SkewY(13.0), result[index])
    }


}