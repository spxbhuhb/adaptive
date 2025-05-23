package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.Qualifier
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.globalTestPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetTypeTest {

    @Test
    fun testGetType() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testFilePath = globalTestPath.resolve("resource.txt")

        val compilation = ResourceCompilation(
            testFilePath, "", "commonTest", testFilePath, testFilePath,
            withFileDefault = false
        )

        val errors = compilation.reports

        // Test case: No resource type found
        val noTypeQualifiers = setOf<Qualifier>()
        assertNull(compilation.getType(testFilePath, noTypeQualifiers))
        assertEquals(1, errors.size)
        assertEquals("Cannot determine resource type for:\n    $testFilePath", errors.first().message)

        // Test case: Ambiguous resource types
        errors.clear()
        val ambiguousQualifiers = setOf(
            ResourceTypeQualifier.parse("file")!!,
            ResourceTypeQualifier.parse("image")!!
        )
        assertNull(compilation.getType(testFilePath, ambiguousQualifiers))
        assertEquals(1, errors.size)
        assertEquals(
            "Ambiguous resource types ([File, Image]) for\n    $testFilePath",
            errors.first().message
        )

        // Test case: Valid single resource type
        errors.clear()
        val validQualifiers = setOf(ResourceTypeQualifier.parse("file")!!)
        assertEquals(ResourceTypeQualifier.parse("file"), compilation.getType(testFilePath, validQualifiers))
        assertEquals(0, errors.size)
    }

}