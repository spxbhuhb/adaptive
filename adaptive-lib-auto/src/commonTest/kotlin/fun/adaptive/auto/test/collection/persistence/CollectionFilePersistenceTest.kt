package `fun`.adaptive.auto.test.collection.persistence

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.internal.persistence.AutoCollectionExport
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.CollectionFilePersistence
import `fun`.adaptive.auto.internal.persistence.ItemFilePersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CollectionFilePersistenceTest {

    @Test
    fun save_and_load_empty() {
        if (GlobalRuntimeContext.platform.isJs) return

        autoCommon()

        val dirPath = clearedTestPath()
        val metaPath = Path(dirPath, "meta.json")

        val persistence = CollectionFilePersistence<TestData>(metaPath, Json) { itemId, item ->
            Path(dirPath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val export = AutoCollectionExport<TestData>(
            meta = AutoMetadata(AutoConnectionInfo.origin<TestData>(false), null, null),
            emptyList()
        )

        persistence.update(export)

        assertTrue(metaPath.exists())

        val import = persistence.load()

        assertNotNull(import)
        assertEquals(export.meta, import.meta)
        assertEquals(export.items, import.items)
    }

    @Test
    fun save_and_load_not_empty() {
        if (GlobalRuntimeContext.platform.isJs) return

        autoCommon()

        val dirPath = clearedTestPath()
        val metaPath = Path(dirPath, "meta.json")

        val persistence = CollectionFilePersistence<TestData>(metaPath, Json) { itemId, item ->
            Path(dirPath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val export = AutoCollectionExport<TestData>(
            meta = AutoMetadata(AutoConnectionInfo.origin<TestData>(false), null, null),
            listOf(
                AutoItemExport<TestData>(
                    meta = null,
                    itemId = LamportTimestamp(0, 2),
                    propertyTimes = listOf(LamportTimestamp(0, 2), LamportTimestamp(0, 2)),
                    item = TestData(12, "ab")
                )
            )
        )

        persistence.update(export)

        assertTrue(metaPath.exists())

        val import = persistence.load()

        assertNotNull(import)
        assertEquals(export.meta, import.meta)
        assertEquals(export.items, import.items)
    }

    @Test
    fun basic() {
        if (GlobalRuntimeContext.platform.isJs) return

        autoCommon()

        val dirPath = clearedTestPath()
        val metaPath = Path(dirPath, "meta.json")

        val td = TestData(12, "ab")

        val persistence = CollectionFilePersistence<TestData>(metaPath, Json) { itemId, item ->
            Path(dirPath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val instance = autoCollectionOrigin(listOf(td), persistence = persistence)

        instance.update(td::i to 23) { true }

        ItemFilePersistence.read<TestData>(Path(dirPath, "0.1.json"), Json).also { import ->
            assertEquals(23, import.item !!.i)
        }

        val otherPersistence = CollectionFilePersistence<TestData>(metaPath, Json) { itemId, item ->
            Path(dirPath, "$itemId.json")
        }

        val other = autoCollectionOrigin(null, persistence = otherPersistence)

        assertEquals(other.value, instance.value)
    }

}