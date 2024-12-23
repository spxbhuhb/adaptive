package `fun`.adaptive.resource

import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.resource.file.FileResourceSet
import `fun`.adaptive.resource.file.Files
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FileTest {

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

        val content = Files.testFile.readAll(environment, reader)

        assertEquals(expected, content.decodeToString())
    }

}

val Files.testFile: FileResourceSet
    get() = CommonMainFiles0.testFile

private object CommonMainFiles0 {
    val testFile by lazy { init_testFile() }
}

private fun init_testFile() =
    FileResourceSet(
        name = "testFile",
        FileResource("adaptiveResources/test.txt", emptySet())
    )