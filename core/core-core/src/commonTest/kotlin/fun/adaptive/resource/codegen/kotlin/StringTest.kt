package `fun`.adaptive.resource.codegen.kotlin

import `fun`.adaptive.code.kotlin.writer.kotlinWriter
import `fun`.adaptive.code.kotlin.writer.kwFile
import `fun`.adaptive.code.kotlin.writer.kwModule
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.resource.string.StringStoreResourceSet
import kotlin.test.Test
import kotlin.test.assertEquals

class StringTest {

    val set =
        StringStoreResourceSet(
            name = "common",
            FileResource("strings/common-cs-CZ.avs", setOf(LanguageQualifier("cs"), RegionQualifier("CZ"))),
            FileResource("strings/common-hu-HU.avs", setOf(LanguageQualifier("hu"), RegionQualifier("HU")))
        )

    @Test
    fun basic() {

        val writer = kotlinWriter {
            kwModule {
                kwFile("CommonStrings0") {
                    stringResource("commonStrings", set, 0, listOf("v1", "v2"))
                }
            }
        }

        writer.render()

        assertEquals(singleSource, writer.modules.first().files.first().renderedSource)
    }

    @Test
    fun chunked() {
        val writer = kotlinWriter {
            kwModule {
                kwFile("CommonStrings0") {
                    stringResource("commonStrings", set, 0, listOf("v1", "v2"))
                }
                kwFile("CommonStrings1") {
                    stringResource("commonStrings", set, 2, listOf("v3", "v4"))
                }
            }
        }

        writer.render()

        val module = writer.modules.first()
        assertEquals(singleSource, module.files.first().renderedSource)
        assertEquals(chunkedSource, module.files[1].renderedSource)

    }

    val singleSource = """
        import `fun`.adaptive.resource.string.Strings
        import `fun`.adaptive.resource.string.StringStoreResourceSet
        import `fun`.adaptive.resource.file.FileResource
        import `fun`.adaptive.resource.LanguageQualifier
        import `fun`.adaptive.resource.RegionQualifier
        
        val commonStrings =
            StringStoreResourceSet(
                name = "common",
                FileResource("strings/common-cs-CZ.avs", setOf(LanguageQualifier("cs"), RegionQualifier("CZ"))),
                FileResource("strings/common-hu-HU.avs", setOf(LanguageQualifier("hu"), RegionQualifier("HU")))
            )
    
        val Strings.v1
            get() = commonStrings.get(0)

        val Strings.v2
            get() = commonStrings.get(1)
    """.trimIndent()

    val chunkedSource = """
       import `fun`.adaptive.resource.string.Strings

       val Strings.v3
           get() = commonStrings.get(0)

       val Strings.v4
           get() = commonStrings.get(1)
    """.trimIndent()
}