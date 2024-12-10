package `fun`.adaptive.resource

import `fun`.adaptive.resource.model.FileResource
import `fun`.adaptive.resource.model.FileResourceSet
import `fun`.adaptive.resource.model.Files
import `fun`.adaptive.resource.model.Strings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StringTest {

    @Test
    fun getFileResource() = runTest {

        val expected = "Hello World!"

        val environment = ResourceEnvironment(
            LanguageQualifier("hu"),
            RegionQualifier("HU"),
            ThemeQualifier.DARK,
            DensityQualifier.HDPI
        )

        val reader = TestResourceReader { expected.encodeToByteArray() }

        val content = Files.testFile.read(environment, reader)

        assertEquals(expected, content.decodeToString())
    }

}

val Strings.testString: String
    get() = CommonMainStrings0.testString

private object CommonMainStrings0 {
    var testString = ""

    fun load(environment: ResourceEnvironment) {
        testString = "Hello World!"
    }
}

private fun init_testFile() =
    FileResourceSet(
        name = "testFile",
        FileResource("adaptiveResources/test.txt", emptySet())
    )