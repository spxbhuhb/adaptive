/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.parse

import hu.simplexion.adaptive.grapics.svg.instruction.ClosePath
import hu.simplexion.adaptive.grapics.svg.instruction.LineTo
import hu.simplexion.adaptive.grapics.svg.instruction.MoveTo
import hu.simplexion.adaptive.grapics.svg.parse.parsePath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ParsePathKtTest {
    @Test
    fun testParsePath() {
        val path = "M 10 10 l 20 20 m 5 5 L 5 5 Z"

        val expectedCommands = listOf(
            MoveTo(10.0, 10.0),
            LineTo(30.0, 30.0),
            MoveTo(35.0, 35.0),
            LineTo(5.0, 5.0),
            ClosePath(5.0, 5.0, 35.0, 35.0)
        )

        val commands = parsePath(path)
        assertEquals(expectedCommands.size, commands.size)

        commands.forEachIndexed { index, actualCommand ->
            val expectedCommand = expectedCommands[index]
            when (actualCommand) {
                is MoveTo -> {
                    assertTrue(expectedCommand is MoveTo)
                    assertEquals(expectedCommand.x, actualCommand.x)
                    assertEquals(expectedCommand.y, actualCommand.y)
                }

                is LineTo -> {
                    assertTrue(expectedCommand is LineTo)
                    assertEquals(expectedCommand.x, actualCommand.x)
                    assertEquals(expectedCommand.y, actualCommand.y)
                }

                is ClosePath -> {
                    assertTrue(expectedCommand is ClosePath)
                    assertEquals(expectedCommand.x1, actualCommand.x1)
                    assertEquals(expectedCommand.y1, actualCommand.y1)
                    assertEquals(expectedCommand.x2, actualCommand.x2)
                    assertEquals(expectedCommand.y2, actualCommand.y2)
                }

                else -> fail("Unexpected command type")
            }
        }
    }
}