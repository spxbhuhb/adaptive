package `fun`.adaptive.resource

import `fun`.adaptive.resource.image.ImageResource
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.resource.image.Images
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ImageTest {

    @Test
    fun getImageResource() = runTest {

        val expected = "Hello World!"

        val environment = ResourceEnvironment(
            LanguageQualifier("hu"),
            RegionQualifier("HU"),
            ThemeQualifier.DARK,
            DensityQualifier.HDPI
        )

        val reader = TestResourceReader { expected.encodeToByteArray() }

        val content = Images.testImage.readAll(environment, reader)

        assertEquals(expected, content.decodeToString())
    }

}

val Images.testImage: ImageResourceSet
    get() = CommonMainImages0.testImage

private object CommonMainImages0 {
    val testImage by lazy { init_testImage() }
}

private fun init_testImage() =
    ImageResourceSet(
        name = "testImage",
        ImageResource("adaptiveResources/test.jpg", emptySet())
    )