package `fun`.adaptive.resource

import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResource
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GraphicsTest {

    @Test
    fun getGraphicsResource() = runTest {

        val expected = "Hello World!"

        val environment = ResourceEnvironment(
            LanguageQualifier("hu"),
            RegionQualifier("HU"),
            ThemeQualifier.DARK,
            DensityQualifier.HDPI
        )

        val reader = TestResourceReader { expected.encodeToByteArray() }

        val content = Graphics.testGraphics.readAll(environment, reader)

        assertEquals(expected, content.decodeToString())
    }

}

val Graphics.testGraphics: GraphicsResourceSet
    get() = CommonMainGraphics0.testGraphics

private object CommonMainGraphics0 {
    val testGraphics by lazy { init_testGraphics() }
}

private fun init_testGraphics() =
    GraphicsResourceSet(
        name = "testImage",
        GraphicsResource("adaptiveResources/test.svg", emptySet())
    )