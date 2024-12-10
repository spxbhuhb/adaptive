package `fun`.adaptive.resource

import `fun`.adaptive.resource.model.FontResource
import `fun`.adaptive.resource.model.FontResourceSet
import `fun`.adaptive.resource.model.Fonts
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FontTest {

    @Test
    fun getFontResource() = runTest {

        val expected = "Hello World!"

        val environment = ResourceEnvironment(
            LanguageQualifier("hu"),
            RegionQualifier("HU"),
            ThemeQualifier.DARK,
            DensityQualifier.HDPI
        )

        val reader = TestResourceReader { expected.encodeToByteArray() }

        val content = Fonts.testFont.read(environment, reader)

        assertEquals(expected, content.decodeToString())
    }

}

val Fonts.testFont: FontResourceSet
    get() = CommonMainFonts0.testFont

private object CommonMainFonts0 {
    val testFont by lazy { init_testFont() }
}

private fun init_testFont() =
    FontResourceSet(
        name = "testFont",
        FontResource("adaptiveResources/test.ttf", emptySet())
    )