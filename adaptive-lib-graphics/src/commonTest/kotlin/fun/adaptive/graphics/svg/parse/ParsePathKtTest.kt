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

    @Test
    fun testParsePath3() {
        println(parsePath("M 216 -176 Q 171 -221 145.5 -280 Q 120 -339 120 -402 T 216 -176 Z"))

        // MoveTo(x=216.0, y=-176.0)
        // QuadraticCurve(x1=171.0, y1=-221.0, x=145.5, y=-280.0),
        // QuadraticCurve(x1=120.0, y1=-339.0, x=120.0, y=-402.0)
        // QuadraticCurve(x1=312.0, y1=-13.0, x=216.0, y=-176.0)
        // ClosePath(x1=216.0, y1=-176.0, x2=216.0, y2=-176.0)]

        //println(parsePath("M216-176q-45-45-70.5-104T120-402q0-63 24-124.5T222-642q35-35 86.5-60t122-39.5Q501-756 591.5-759t202.5 7q8 106 5 195t-16.5 160.5q-13.5 71.5-38 125T684-182q-53 53-112.5 77.5T450-80q-65 0-127-25.5T216-176Zm112-16q29 17 59.5 24.5T450-160q46 0 91-18.5t86-59.5q18-18 36.5-50.5t32-85Q709-426 716-500.5t2-177.5q-49-2-110.5-1.5T485-670q-61 9-116 29t-90 55q-45 45-62 89t-17 85q0 59 22.5 103.5T262-246q42-80 111-153.5T534-520q-72 63-125.5 142.5T328-192Zm0 0Zm0 0Z"))
        // MoveTo(x=216.0, y=-176.0),
        // QuadraticCurve(x1=171.0, y1=-221.0, x=145.5, y=-280.0),
        // QuadraticCurve(x1=69.0, y1=-583.0, x=120.0, y=-402.0),
        // QuadraticCurve(x1=120.0, y1=-465.0, x=144.0, y=-526.5),
        // QuadraticCurve(x1=324.0, y1=-819.0, x=222.0, y=-642.0),
        // QuadraticCurve(x1=257.0, y1=-677.0, x=308.5, y=-702.0),
        // QuadraticCurve(x1=604.0, y1=-806.0, x=430.5, y=-741.5),
        // QuadraticCurve(x1=501.0, y1=-756.0, x=591.5, y=-759.0),
        // QuadraticCurve(x1=1087.0, y1=-748.0, x=794.0, y=-752.0),
        // QuadraticCurve(x1=802.0, y1=-646.0, x=799.0, y=-557.0),
        // QuadraticCurve(x1=763.0, y1=-147.0, x=782.5, y=-396.5),
        // QuadraticCurve(x1=769.0, y1=-325.0, x=744.5, y=-271.5),
        // QuadraticCurve(x1=599.0, y1=-39.0, x=684.0, y=-182.0),
        // QuadraticCurve(x1=631.0, y1=-129.0, x=571.5, y=-104.5),
        // QuadraticCurve(x1=269.0, y1=-31.0, x=450.0, y=-80.0),
        // QuadraticCurve(x1=385.0, y1=-80.0, x=323.0, y=-105.5),
        // QuadraticCurve(x1=47.0, y1=-272.0, x=216.0, y=-176.0),
        // ClosePath(x1=216.0, y1=-176.0, x2=216.0, y2=-176.0),
        // MoveTo(x=328.0, y=-192.0),
        // QuadraticCurve(x1=357.0, y1=-175.0, x=387.5, y=-167.5),
        // QuadraticCurve(x1=543.0, y1=-145.0, x=450.0, y=-160.0),
        // QuadraticCurve(x1=496.0, y1=-160.0, x=541.0, y=-178.5),
        // QuadraticCurve(x1=758.0, y1=-316.0, x=627.0, y=-238.0),
        // QuadraticCurve(x1=645.0, y1=-256.0, x=663.5, y=-288.5),
        // QuadraticCurve(x1=746.0, y1=-491.0, x=695.5, y=-373.5),
        // QuadraticCurve(x1=709.0, y1=-426.0, x=716.0, y=-500.5),
        // QuadraticCurve(x1=727.0, y1=-930.0, x=718.0, y=-678.0),
        // QuadraticCurve(x1=669.0, y1=-680.0, x=607.5, y=-679.5),
        // QuadraticCurve(x1=301.0, y1=-660.0, x=485.0, y=-670.0),
        // QuadraticCurve(x1=424.0, y1=-661.0, x=369.0, y=-641.0),
        // QuadraticCurve(x1=134.0, y1=-511.0, x=279.0, y=-586.0),
        // QuadraticCurve(x1=234.0, y1=-541.0, x=217.0, y=-497.0),
        // QuadraticCurve(x1=166.0, y1=-283.0, x=200.0, y=-412.0),
        // QuadraticCurve(x1=200.0, y1=-353.0, x=222.5, y=-308.5),
        // QuadraticCurve(x1=324.0, y1=-139.0, x=262.0, y=-246.0),
        // QuadraticCurve(x1=304.0, y1=-326.0, x=373.0, y=-399.5),
        // QuadraticCurve(x1=764.0, y1=-714.0, x=534.0, y=-520.0),
        // QuadraticCurve(x1=462.0, y1=-457.0, x=408.5, y=-377.5),
        // QuadraticCurve(x1=194.0, y1=73.0, x=328.0, y=-192.0),
        // ClosePath(x1=328.0, y1=-192.0, x2=328.0, y2=-192.0),
        // MoveTo(x=328.0, y=-192.0),
        // ClosePath(x1=328.0, y1=-192.0, x2=328.0, y2=-192.0),
        // MoveTo(x=328.0, y=-192.0),
        // ClosePath(x1=328.0, y1=-192.0, x2=328.0, y2=-192.0)
    }
}