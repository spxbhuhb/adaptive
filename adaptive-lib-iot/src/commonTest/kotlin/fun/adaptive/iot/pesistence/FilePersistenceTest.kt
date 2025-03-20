package `fun`.adaptive.iot.pesistence

import `fun`.adaptive.iot.item.AioStatus
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.builtin.AvString
import `fun`.adaptive.iot.value.persistence.FilePersistence
import `fun`.adaptive.utility.*
import kotlinx.datetime.Clock.System.now
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FilePersistenceTest {

    @Test
    fun `test save and load value`() {
        val testRoot = clearedTestPath()
        val map = mutableMapOf<AioValueId, AioValue>()
        val persistence = FilePersistence(testRoot, 2)

        val value = AvString(UUID("48852c46-8e5a-40a1-a9c8-e757c6f58200"), now(), AioStatus.OK, "TestData")
        persistence.saveValue(value)

        val savedFile = persistence.store.pathFor(value.uuid)
        assertTrue(savedFile.exists(), "Saved file should exist")

        persistence.loadValues(map)

        assertEquals(value, map[value.uuid], "Loaded value should match saved value")
    }

    @Test
    fun `test load value with invalid data`() {
        val testRoot = clearedTestPath()
        val map = mutableMapOf<AioValueId, AioValue>()
        val persistence = FilePersistence(testRoot, 2)

        val invalidFileDir = testRoot.resolve("00/82").ensure()
        invalidFileDir.resolve("48852c46-8e5a-40a1-a9c8-e757c6f58200.json").write("corrupt data")

        assertFailsWith<Exception> { persistence.loadValues(map) }
    }

    @Test
    fun `test load multiple values`() {
        val testRoot = clearedTestPath()
        val map = mutableMapOf<AioValueId, AioValue>()
        val persistence = FilePersistence(testRoot, 2)

        val value1 = AvString(UUID("48852c46-8e5a-40a1-a9c8-e757c6f58200"), now(), AioStatus.OK, "Data1")
        val value2 = AvString(UUID("123e4567-e89b-12d3-a456-426614174000"), now(), AioStatus.OK, "Data2")

        persistence.saveValue(value1)
        persistence.saveValue(value2)

        persistence.loadValues(map)

        assertEquals(value1, map[value1.uuid])
        assertEquals(value2, map[value2.uuid])
    }
}