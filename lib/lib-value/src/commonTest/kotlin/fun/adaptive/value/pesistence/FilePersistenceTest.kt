package `fun`.adaptive.value.pesistence

import `fun`.adaptive.persistence.clearedTestPath
import `fun`.adaptive.persistence.ensure
import `fun`.adaptive.persistence.exists
import `fun`.adaptive.persistence.resolve
import `fun`.adaptive.persistence.write
import `fun`.adaptive.utility.*
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.persistence.FilePersistence
import kotlin.time.Clock.System.now
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FilePersistenceTest {

    @Test
    @JsName("testSaveAndLoadValue")
    fun `test save and load value`() {
        val testRoot = clearedTestPath()
        val map = mutableMapOf<AvValueId, AvValue<*>>()
        val persistence = FilePersistence(testRoot, 2)

        val value = AvValue(UUID("48852c46-8e5a-40a1-a9c8-e757c6f58200"), now(), spec = "TestData")
        persistence.saveValue(value)

        val savedFile = persistence.store.pathFor(value.uuid)
        assertTrue(savedFile.exists(), "Saved file should exist")

        persistence.loadValues(map)

        assertEquals(value, map[value.uuid], "Loaded value should match saved value")
    }

    @Test
    @JsName("testLoadValueWithInvalidData")
    fun `test load value with invalid data`() {
        val testRoot = clearedTestPath()
        val map = mutableMapOf<AvValueId, AvValue<*>>()
        val persistence = FilePersistence(testRoot, 2)

        val invalidFileDir = testRoot.resolve("00/82").ensure()
        invalidFileDir.resolve("48852c46-8e5a-40a1-a9c8-e757c6f58200.json").write("corrupt data")

        assertFailsWith<Exception> { persistence.loadValues(map) }
    }

    @Test
    @JsName("testLoadMultipleValues")
    fun `test load multiple values`() {
        val testRoot = clearedTestPath()
        val map = mutableMapOf<AvValueId, AvValue<*>>()
        val persistence = FilePersistence(testRoot, 2)

        val value1 = AvValue(UUID("48852c46-8e5a-40a1-a9c8-e757c6f58200"), now(), spec = "Data1")
        val value2 = AvValue(UUID("123e4567-e89b-12d3-a456-426614174000"), now(), spec = "Data2")

        persistence.saveValue(value1)
        persistence.saveValue(value2)

        persistence.loadValues(map)

        assertEquals(value1, map[value1.uuid])
        assertEquals(value2, map[value2.uuid])
    }
}