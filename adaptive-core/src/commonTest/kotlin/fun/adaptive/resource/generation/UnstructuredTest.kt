package `fun`.adaptive.resource.generation

import `fun`.adaptive.kotlin.writer.kotlinWriter
import `fun`.adaptive.kotlin.writer.kwFile
import `fun`.adaptive.kotlin.writer.kwModule
import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.resource.file.FileResourceSet
import kotlin.test.Test
import kotlin.test.assertEquals

class UnstructuredTest {

    @Test
    fun basic() {
        val sets = listOf(
            FileResourceSet(
                name = "testFile",
                FileResource(
                    "adaptiveResources/test-hu-HU.txt",
                    setOf(
                        LanguageQualifier("hu"),
                        RegionQualifier("HU")
                    )
                ),
                FileResource(
                    "adaptiveResources/test-cs-CZ.txt",
                    setOf(
                        LanguageQualifier("cs"),
                        RegionQualifier("CZ"),
                        ThemeQualifier.DARK,
                        DensityQualifier.HDPI
                    )
                )
            )
        )

        val writer = kotlinWriter {
            kwModule {
                kwFile("commonMainFiles0") {
                    unstructuredResource("CommonMainFiles0", sets)
                }
            }
        }

        writer.render()

        assertEquals(filesSource, writer.modules.first().files.first().renderedSource)
    }

    val filesSource = """
        import `fun`.adaptive.resource.file.Files
        import `fun`.adaptive.resource.file.FileResource
        import `fun`.adaptive.resource.file.FileResourceSet
        import `fun`.adaptive.resource.LanguageQualifier
        import `fun`.adaptive.resource.RegionQualifier
        import `fun`.adaptive.resource.ThemeQualifier
        import `fun`.adaptive.resource.DensityQualifier

        private object CommonMainFiles0 {

            val testFile by lazy { init_testFile() }

        }

        val Files.testFile
            get() = CommonMainFiles0.testFile

        private fun init_testFile() =
            FileResourceSet(
                name = "testFile",
                FileResource("adaptiveResources/test-hu-HU.txt", setOf(LanguageQualifier("hu"), RegionQualifier("HU"))),
                FileResource("adaptiveResources/test-cs-CZ.txt", setOf(LanguageQualifier("cs"), RegionQualifier("CZ"), ThemeQualifier.DARK, DensityQualifier.HDPI))
            )
    """.trimIndent()
}