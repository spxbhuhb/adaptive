import hu.simplexion.adaptive.lib.grapics.canvas.ClosePath
import hu.simplexion.adaptive.lib.grapics.canvas.LineTo
import hu.simplexion.adaptive.lib.grapics.canvas.MoveTo
import hu.simplexion.adaptive.lib.grapics.canvas.parsePath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class PathKtTest {
    @Test
    fun testParsePath() {
        val path = "M 10 10 l 20 20 m 5 5 L 5 5 Z"

        val expectedCommands = listOf(
            MoveTo(10f, 10f),
            LineTo(30f, 30f),
            MoveTo(35f, 35f),
            LineTo(5f, 5f),
            ClosePath(5f, 5f, 35f, 35f)
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