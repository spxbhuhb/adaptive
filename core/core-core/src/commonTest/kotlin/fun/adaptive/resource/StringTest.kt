package `fun`.adaptive.resource

import `fun`.adaptive.resource.avs.AvsWriter
import `fun`.adaptive.resource.file.FileResource
import `fun`.adaptive.resource.string.StringStoreResourceSet
import `fun`.adaptive.resource.string.Strings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class StringTest {

    @Test
    fun basic() = runTest {

        val environment = ResourceEnvironment(
            LanguageQualifier("hu"),
            RegionQualifier("HU"),
            ThemeQualifier.DARK,
            DensityQualifier.HDPI
        )

        val v1 = "123"
        val v2 = "456"

        val writer = AvsWriter()
        writer += byteArrayOf(2) + "v1".encodeToByteArray() + v1.encodeToByteArray()
        writer += byteArrayOf(2) + "v2".encodeToByteArray() + v2.encodeToByteArray()
        val binary = writer.pack()

        val reader = TestResourceReader { binary }

        commonStrings.load(environment, reader)

        assertEquals(v1, Strings.v1)
        assertEquals(v2, Strings.v2)
    }

}

val commonStrings =
    StringStoreResourceSet(
        name = "common",
        FileResource("strings/common-cs-CZ.avs", setOf(LanguageQualifier("cs"), RegionQualifier("CZ"))),
        FileResource("strings/common-hu-HU.avs", setOf(LanguageQualifier("hu"), RegionQualifier("HU")))
    )

@Suppress("UnusedReceiverParameter")
val Strings.v1
    get() = commonStrings.get("v1")

@Suppress("UnusedReceiverParameter")
val Strings.v2
    get() = commonStrings.get("v2")