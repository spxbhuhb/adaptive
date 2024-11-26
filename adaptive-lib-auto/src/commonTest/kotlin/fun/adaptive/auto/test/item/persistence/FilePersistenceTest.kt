package `fun`.adaptive.auto.test.item.persistence

import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.internal.persistence.AutoItemExport
import `fun`.adaptive.auto.internal.persistence.ItemFilePersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.auto.model.AutoMetadata
import `fun`.adaptive.auto.model.ITEM_ID_ORIGIN
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.utility.clearTestPath
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FilePersistenceTest {

    @Test
    fun basic() {
        autoCommon()

        val dirPath = clearTestPath()
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

        assertEquals(export.meta, import.meta)
        assertEquals(export.itemId, import.itemId)
        assertEquals(export.propertyTimes, import.propertyTimes)
        assertEquals(export.item, import.item)
    }

    @Test
    fun noMeta() {
        autoCommon()

        val dirPath = clearTestPath()
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

        assertEquals(export.meta, import.meta)
        assertEquals(export.itemId, import.itemId)
        assertEquals(export.propertyTimes, import.propertyTimes)
        assertEquals(export.item, import.item)
    }
}