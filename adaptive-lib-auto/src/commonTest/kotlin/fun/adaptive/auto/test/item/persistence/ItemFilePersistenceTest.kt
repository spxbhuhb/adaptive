package `fun`.adaptive.auto.test.item.persistence

import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.ItemFilePersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ITEM_ID_ORIGIN
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

class ItemFilePersistenceTest {

    @Test
    fun saveAndLoad() {
        if (GlobalRuntimeContext.platform.isJs) return

        autoCommon()

        val dirPath = clearedTestPath()
        val filePath = Path(dirPath, "test.json")

        val persistence = ItemFilePersistence<TestData>(filePath, Json)

        val export = AutoItemExport<TestData>(
            meta = AutoMetadata(AutoConnectionInfo.origin<TestData>(false), null, null),
            itemId = ITEM_ID_ORIGIN,
            propertyTimes = listOf(LamportTimestamp.ORIGIN, LamportTimestamp.ORIGIN),
            item = TestData(12, "ab")
        )

        persistence.save(export)

        assertTrue(filePath.exists())

        val import = persistence.load()

        assertNotNull(import)
        assertEquals(export.meta, import.meta)
        assertEquals(export.itemId, import.itemId)
        assertEquals(export.propertyTimes, import.propertyTimes)
        assertEquals(export.item, import.item)
    }

    @Test
    fun saveAndLoadNoMeta() {
        if (GlobalRuntimeContext.platform.isJs()) return

        autoCommon()

        val dirPath = clearedTestPath()
        val filePath = Path(dirPath, "test.json")

        val persistence = ItemFilePersistence<TestData>(filePath, Json)

        val export = AutoItemExport<TestData>(
            meta = null,
            itemId = ITEM_ID_ORIGIN,
            propertyTimes = listOf(LamportTimestamp.ORIGIN, LamportTimestamp.ORIGIN),
            item = TestData(12, "ab")
        )

        persistence.save(export)

        assertTrue(filePath.exists())

        val import = persistence.load()

        assertNotNull(import)
        assertEquals(export.meta, import.meta)
        assertEquals(export.itemId, import.itemId)
        assertEquals(export.propertyTimes, import.propertyTimes)
        assertEquals(export.item, import.item)
    }

    @Test
    fun basic() {
        if (GlobalRuntimeContext.platform.isJs()) return

        autoCommon()

        val dirPath = clearedTestPath()
        val filePath = Path(dirPath, "test.json")

        val td = TestData(12, "ab")

        val instance = autoItemOrigin(td, persistence = ItemFilePersistence<TestData>(filePath, Json))

        ItemFilePersistence.read<TestData>(filePath, Json).also { import ->
            assertEquals(instance.handle, import.meta?.connection?.connectingHandle)
            assertEquals(td, import.item)
        }

        ItemFilePersistence.read<TestData>(filePath, Json).also { import ->
            assertEquals(12, import.item!!.i)
        }

        instance.update(td::i to 23)

        ItemFilePersistence.read<TestData>(filePath, Json).also { import ->
            assertEquals(23, import.item!!.i)
        }

        val other = autoItemOrigin(null, persistence = ItemFilePersistence<TestData>(filePath, Json))

        assertEquals(other.value, instance.value)

    }

}