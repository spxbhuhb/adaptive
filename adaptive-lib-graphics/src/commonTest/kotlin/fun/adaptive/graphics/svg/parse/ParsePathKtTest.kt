/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.parse

import `fun`.adaptive.graphics.svg.instruction.*
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

    @Test
    fun testCubicCurveSmoothAbsoluteNoPrevious() {
        val path = parsePath("M 15 25 S 10 20 30 40 Z")

        val end = path.last() as ClosePath
        assertEquals(end.x1, 30.0, "Incorrect new x position")
        assertEquals(end.y1, 40.0, "Incorrect new y position")

        // Since there was no previous command, the coordinates for the existing position
        // (x1,y1) would be same as position's x and y coordinates.

        val expectedCommand = CubicCurve(15.0, 25.0, 10.0, 20.0, 30.0, 40.0)
        assertTrue(path[1] is CubicCurve)
        assertEquals(expectedCommand, path[1] as CubicCurve)
    }

    @Test
    fun testCubicCurveSmoothAbsoluteWithPrevious() {
        val path = parsePath("C 1 2 3 4 10 20 S 10 20 30 40 Z")

        val end = path.last() as ClosePath
        assertEquals(end.x1, 30.0, "Incorrect new x position")
        assertEquals(end.y1, 40.0, "Incorrect new y position")

        // Since there was a previous command, the coordinates for the existing position (x1,y1)
        // would be calculated from position's x and y coordinates and last position in the previous command

        val expectedCommand = CubicCurve((2 * 10) - 3.0, (2 * 20) - 4.0, 10.0, 20.0, 30.0, 40.0)
        assertTrue(path[1] is CubicCurve)
        assertEquals(expectedCommand, path[1] as CubicCurve)
    }

    @Test
    fun testCubicCurveSmoothAbsoluteFollowUp() {
        //Test with 8 parameters and have previous commands

        val path = parsePath("C 1 2 3 4 10 20 S 10 20 30 40 S 50 60 70 80 Z")

        val expectedCommand = CubicCurve((2 * 30) - 10.0, (2 * 40) - 20.0, 50.0, 60.0, 70.0, 80.0)
        assertTrue(path[2] is CubicCurve)
        assertEquals(expectedCommand, path[2] as CubicCurve)
    }

    @Test
    fun testParsePath2() {
        listOf(
            "q-27.62 0-46.12-18.5",
            "Q120-237 120-264.62",
            "q0-27.62 18.5-46.12",
            "Q157-760 184.62-760",
            "q27.62 0 46.12 18.5",
            "Q840-723 840-695.38",
            "q0 27.62-18.5 46.12",
            "Q803-200 775.38-200",
            "q0 10.77 6.92 17.7 6.93 6.92 17.7 6.92",
            "q10.77 0 17.7-6.92 6.92-6.93 6.92-17.7",
            "q0 10.77 6.92 17.7 6.93 6.92 17.7 6.92H160v-444.62Z"
        ).forEach {
            println(it)
            parsePath(it)
        }
    }

}