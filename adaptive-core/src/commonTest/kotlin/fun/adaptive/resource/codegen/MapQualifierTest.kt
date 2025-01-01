package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.*
import `fun`.adaptive.utility.PlatformType
import `fun`.adaptive.utility.platformType
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.testPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapQualifierTest {

    @Test
    fun testMapQualifier() {
        if (platformType == PlatformType.JsBrowser) return

        val testFilePath = testPath.resolve("resource.txt")

        with(
            ResourceCompilation(
                testFilePath, "", "commonTest", testFilePath, testFilePath
            )
        ) {

            ResourceTypeQualifier.entries.forEach {
                assertEquals(it, mapQualifier(it.name.lowercase()))
                assertTrue { reports.isEmpty() }
            }

            assertEquals(LanguageQualifier("hu"), mapQualifier("hu"))
            assertTrue { reports.isEmpty() }

            assertEquals(RegionQualifier("HU"), mapQualifier("HU"))
            assertTrue { reports.isEmpty() }

            assertEquals(RegionQualifier("rHU"), mapQualifier("rHU"))
            assertTrue { reports.isEmpty() }

            assertEquals(RegionQualifier("HUN"), mapQualifier("HUN"))
            assertTrue { reports.isEmpty() }

            ThemeQualifier.entries.forEach {
                if (it != ThemeQualifier.INVALID) {
                    assertEquals(it, mapQualifier(it.name.lowercase()))
                    assertTrue { reports.isEmpty() }
                }
            }

            DensityQualifier.entries.forEach {
                if (it != DensityQualifier.INVALID) {
                    assertEquals(it, mapQualifier(it.name.lowercase()))
                    assertTrue { reports.isEmpty() }
                }
            }

            assertNull(mapQualifier("unknown"))
            assertTrue { reports.isNotEmpty() }
        }


    }
}