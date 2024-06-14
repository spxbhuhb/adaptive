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
        assertEquals(Translate(- 10f, - 20f), result[index ++])
        assertEquals(Translate(15f, 0f), result[index ++])

        assertEquals(Scale(2f, 2f), result[index ++])
        assertEquals(Scale(3f, 4f), result[index ++])

        assertEquals(Rotate(45f, 0f, 0f), result[index ++])
        assertEquals(Rotate(45f, 46f, 47f), result[index ++])

        assertEquals(Matrix(0f, 1f, 2f, 3f, 4f, 5f), result[index++])

        assertEquals(SkewX(12f), result[index ++])
        assertEquals(SkewY(13f), result[index])
    }


}