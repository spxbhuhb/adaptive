package `fun`.adaptive.resource.codegen

import `fun`.adaptive.resource.DensityQualifier
import `fun`.adaptive.resource.ResourceFileSet
import `fun`.adaptive.resource.ResourceTypeQualifier
import `fun`.adaptive.resource.ThemeQualifier
import kotlinx.io.files.Path
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapToResourceFileTest {

    @Test
    @JsName("testMapToResourceFileCreatesNewResourceFileSet")
    fun `test mapToResourceFile creates new ResourceFileSet when none exists`() {
        val (mappedResources, errors) = test(
            Path("/resources/image/logo-dark-mdpi.png"),
        )

        val resourceSet = mappedResources[ResourceTypeQualifier.Image]?.get("logo")
        assertTrue { errors.isEmpty() }
        assertTrue { resourceSet != null }

        val files = resourceSet !!.files
        assertEquals(1, files.size)
        val file = files.first()
        assertEquals("/image/logo-dark-mdpi.png", file.path.toString())
        assertEquals(setOf(ResourceTypeQualifier.Image, ThemeQualifier.DARK, DensityQualifier.MDPI), file.qualifiers)
    }

    @Test
    @JsName("testMapToResourceFileAddsToExistingResourceFileSet")
    fun `test mapToResourceFile adds to existing ResourceFileSet`() {
        val (mappedResources, errors) = test(
            Path("/resources/icon1-mdpi-image.png"),
            Path("/resources/icon1-hdpi-image.png")
        )

        val resourceSet = mappedResources[ResourceTypeQualifier.Image]?.get("icon1")
        assertTrue { errors.isEmpty() }
        assertTrue { resourceSet != null }
        assertEquals(2, resourceSet !!.files.size)
    }

    @Test
    @JsName("testMapToResourceFileAddsErrorOnDuplicateQualifiers")
    fun `test mapToResourceFile adds error on duplicate qualifiers`() {
        val (mappedResources, errors) = test(
            Path("/resources/icon1-mdpi-image.png"),
            Path("/resources/image/icon1-mdpi.png")
        )

        assertEquals(1, errors.size)
        assertTrue { errors[0].contains("Resource files with the same qualifier") }
    }

    @Test
    @JsName("testMapToResourceFileAddsErrorOnAmbiguousResourceTypes")
    fun `test mapToResourceFile adds error on ambiguous resource types`() {
        val (mappedResources, errors) = test(
            Path("/resources/file/icon1-mdpi-image.png")
        )

        assertTrue { mappedResources.all { it.value.isEmpty() }}
        assertEquals(1, errors.size)
        assertTrue { errors[0].contains("Ambiguous resource types") }
    }

    @Test
    @JsName("testMapToResourceFileAddsErrorOnUnknownResourceType")
    fun `test mapToResourceFile adds error on unknown qualifier`() {

        val (mappedResources, errors) = test(
            Path("/resources/icon1-unknown.png")
        )

        assertTrue { mappedResources.all { it.value.isEmpty() }}
        assertEquals(1, errors.size)
        assertTrue { errors[0].contains("Unknown qualifier") }
    }

    fun test(
        vararg paths: Path
    ): Pair<Map<ResourceTypeQualifier, MutableMap<String, ResourceFileSet<*>>>, MutableList<String>> {

        val prefix = "/resources"
        val errors = mutableListOf<String>()

        val resourceSetsByType = ResourceTypeQualifier.entries
            .map { it to mutableMapOf<String, ResourceFileSet<*>>() }
            .associate { it }

        paths.forEach {
            mapToResourceFile(prefix, resourceSetsByType, errors, it)
        }

        return Pair(resourceSetsByType, errors)
    }

}