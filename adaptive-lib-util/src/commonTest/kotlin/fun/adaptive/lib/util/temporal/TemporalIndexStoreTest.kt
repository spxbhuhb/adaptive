package `fun`.adaptive.lib.util.temporal

import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexEntry
import `fun`.adaptive.lib.util.temporal.model.TemporalIndexHeader
import `fun`.adaptive.lib.util.utilCommon
import `fun`.adaptive.utility.*
import kotlinx.datetime.Clock.System.now
import kotlin.test.*

class TemporalIndexStoreTest {

    val indexName = "index.pb"

    @BeforeTest
    fun setup() {
        utilCommon()
    }

    @Test
    fun `store initializes correctly when file does not exist`() {
        val testDir = clearedTestPath()
        val path = testDir.resolve(indexName)
        val store = TemporalIndexStore(UUID(), path)

        store.initialize()

        assertTrue(path.exists(), "File should be created after initialization")
        assertTrue(store.initialized, "Store should be marked as initialized")
        assertTrue(store.entries.isEmpty(), "Entries should be empty on first initialization")
    }

    @Test
    fun `store initializes correctly when file exists`() {
        val path = clearedTestPath().resolve(indexName)
        val store = TemporalIndexStore(UUID(), path, initialize = false)

        val initialEntries = listOf(
            TemporalIndexEntry(now(), UUID(), 100L),
            TemporalIndexEntry(now(), UUID(), 200L)
        )
        val initialIndex = TemporalIndexHeader(TemporalIndexStore.V1, store.uuid)
        path.write(initialIndex.encodeToProtoByteArray())
        initialEntries.forEach { path.append(it.encodeToProtoByteArray()) }

        store.initialize()

        assertTrue(store.initialized, "Store should be marked as initialized")
        assertEquals(initialEntries, store.entries, "Entries should be loaded correctly from file")
    }

    @Test
    fun `store rejects initialization if UUID does not match`() {
        val path = clearedTestPath().resolve(indexName)
        val store = TemporalIndexStore(UUID(), path, initialize = false)

        val initialIndex = TemporalIndexHeader(TemporalIndexStore.V1, UUID())
        path.write(initialIndex.encodeToProtoByteArray())

        val exception = assertFailsWith<IllegalStateException> {
            store.initialize()
        }

        assertTrue(exception.message !!.contains("uuid mismatch"), "Should reject mismatched UUIDs")
    }

    @Test
    fun `add entry updates store and persists to file`() {
        val path = clearedTestPath().resolve(indexName)
        val store = TemporalIndexStore(UUID(), path)
        store.initialize()

        val newEntry = TemporalIndexEntry(now(), UUID(), 300L)
        store.append(newEntry)

        assertEquals(1, store.entries.size, "Entry should be added to store")
        assertEquals(newEntry, store.entries.last(), "Last entry should be the one just added")

        // Reload the store to check persistence
        val reloadedStore = TemporalIndexStore(store.uuid, path)
        reloadedStore.initialize()

        assertEquals(1, reloadedStore.entries.size, "Entry should be persisted to file")
        assertEquals(newEntry, reloadedStore.entries.last(), "Last entry should be the persisted entry")
    }

    @Test
    fun `latest returns last entry or null if empty`() {
        val path = clearedTestPath().resolve(indexName)
        val store = TemporalIndexStore(UUID(), path)
        store.initialize()

        assertNull(store.latest, "Latest should be null when store is empty")

        val newEntry = TemporalIndexEntry(now(), UUID(), 400L)
        store.append(newEntry)

        assertEquals(newEntry, store.latest, "Latest should return the last added entry")
    }
}