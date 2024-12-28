package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.LanguageQualifier
import `fun`.adaptive.resource.RegionQualifier
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.ThemeQualifier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapQualifierTest {

    @Test
    fun testMapQualifier() {
        val errors = mutableListOf<String>()

        ResourceTypeQualifier.entries.forEach {
            assertEquals(it, mapQualifier(it.name.lowercase(), errors))
            assertTrue { errors.isEmpty() }
        }

        assertEquals(LanguageQualifier("hu"), mapQualifier("hu", errors))
        assertTrue { errors.isEmpty() }

        assertEquals(RegionQualifier("HU"), mapQualifier("HU", errors))
        assertTrue { errors.isEmpty() }

        assertEquals(RegionQualifier("rHU"), mapQualifier("rHU", errors))
        assertTrue { errors.isEmpty() }

        assertEquals(RegionQualifier("HUN"), mapQualifier("HUN", errors))
        assertTrue { errors.isEmpty() }

        ThemeQualifier.entries.forEach {
            if (it != ThemeQualifier.INVALID) {
                assertEquals(it, mapQualifier(it.name.lowercase(), errors))
                assertTrue { errors.isEmpty() }
            }
        }

        DensityQualifier.entries.forEach {
            if (it != DensityQualifier.INVALID) {
                assertEquals(it, mapQualifier(it.name.lowercase(), errors))
                assertTrue { errors.isEmpty() }
            }
        }

        assertNull(mapQualifier("unknown", errors))
        assertTrue { errors.isNotEmpty() }
    }
}