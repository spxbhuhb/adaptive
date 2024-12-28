package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceTypeQualifier
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetTypeTest {

    @Test
    fun testGetType() {
        val errors = mutableListOf<String>()
        val testPath = Path("/test/path/to/resource.txt")

        // Test case: No resource type found
        val noTypeQualifiers = setOf<Qualifier>()
        assertNull(getType(testPath, noTypeQualifiers, errors))
        assertEquals(1, errors.size)
        assertEquals("Cannot determine resource type for:\n    $testPath", errors.first())

        // Test case: Ambiguous resource types
        errors.clear()
        val ambiguousQualifiers = setOf(
            ResourceTypeQualifier.parse("file")!!,
            ResourceTypeQualifier.parse("image")!!
        )
        assertNull(getType(testPath, ambiguousQualifiers, errors))
        assertEquals(1, errors.size)
        assertEquals(
            "Ambiguous resource types ([File, Image]) for\n    $testPath",
            errors.first()
        )

        // Test case: Valid single resource type
        errors.clear()
        val validQualifiers = setOf(ResourceTypeQualifier.parse("file")!!)
        assertEquals(ResourceTypeQualifier.parse("file"), getType(testPath, validQualifiers, errors))
        assertEquals(0, errors.size)
    }

}