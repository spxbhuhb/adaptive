/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.logic

import hu.simplexion.adaptive.ui.common.instruction.ColTemplate
import hu.simplexion.adaptive.ui.common.instruction.dp
import hu.simplexion.adaptive.ui.common.instruction.fr
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class GridTest {

    @Test
    fun testDistribute() {

        val tracks = ColTemplate(100.dp, 0.25.fr, 0.75.fr).tracks
        val availableSpace = 400f

        val result = distribute(availableSpace, tracks)

        // track[0] is fixed size:  using defined value
        // track[1] is fractional:  using (availableSpace - sum_of_fixed) * fraction
        val expectedResults = floatArrayOf(0f, 100f, 100f + (300f * 0.25f), 400f)

        assertContentEquals(expectedResults, result)
    }

    data class TestGridCell(
        override var rowIndex: Int,
        override var colIndex: Int,
        override val gridRow: Int?,
        override val gridCol: Int?,
        override val rowSpan: Int,
        override val colSpan: Int
    ) : GridCell

    @Test
    fun testPlaceFragmentsFail() {
        val gridCells = listOf(
            TestGridCell(-1, -1, null, null, 3, 3),  // (gridRow, grilCol) is (null, null), which implies it can be place anywhere
            TestGridCell(-1, -1,1, 1, 2, 2),        // It should be specifically placed at (1, 1)
            TestGridCell(-1, -1,5, 5, 1, 1)         // This cell cannot be placed because it's out of grid.
        )

        try {
            placeFragments(gridCells, 4, 4)
            fail("Exception expected")              // Expecting an IllegalStateException because the last cell cannot be placed.
        } catch (e: Exception) {
            assertTrue(e is IllegalStateException)
        }
    }

    @Test
    fun testPlaceFragmentsSuccess() {

        val gridCells = listOf(
            TestGridCell(-1, -1,null, null, 1, 5),
            TestGridCell(-1, -1,2, 2, 1, 1),
            TestGridCell(-1, -1,2, 4, 1, 1),
            TestGridCell(-1, -1,3, 2, 1, 3),
        )

        placeFragments(gridCells, 3, 5)

        val expectedCells: List<TestGridCell> = listOf(
            TestGridCell(0, 0,null, null, 1, 5),
            TestGridCell(1, 1,2, 2, 1, 1),
            TestGridCell(1, 3,2, 4, 1, 1),
            TestGridCell(2, 1, 3, 2, 1, 3),
        )

        assertContentEquals(expectedCells, gridCells)
    }
}