package `fun`.adaptive.auto.test.collection.persistence

import `fun`.adaptive.auto.api.autoCollectionNode
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.internal.persistence.CollectionFilePersistence
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.auto.test.support.wait
import `fun`.adaptive.lib.util.path.DiffType
import `fun`.adaptive.lib.util.path.diff
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectionNodeFilePersistenceTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")

    val list_empty = listOf<TestData>()
    val list_12 = listOf(td12)
    val list_23 = listOf(td23)
    val list_12_23 = listOf(td12, td23)

    @Test
    fun basic() {
        autoCommon()

        val dirPath = clearedTestPath()

        // ---- origin setup --------

        val originPath = Path(dirPath, "origin").ensure()
        val originMetaPath = Path(originPath, "meta.json")

        val originPersistence = CollectionFilePersistence<TestData>(originMetaPath, Json) { itemId, item ->
            Path(originPath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val origin = autoCollectionOrigin(list_12, persistence = originPersistence, trace = true)

        // ---- node setup --------

        val nodePath = Path(dirPath, "node").ensure()
        val nodeMetaPath = Path(nodePath, "meta.json")

        val nodePersistence = CollectionFilePersistence<TestData>(nodeMetaPath, Json) { itemId, item ->
            Path(nodePath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val node = autoCollectionNode(origin, persistence = nodePersistence, trace = true)

        // ---- test -------

        origin.update(td12::i to 23) { true }
        origin.add(td23)

        wait(origin, node)

        assertTrue(originMetaPath.exists())
        assertTrue(nodeMetaPath.exists())

        // ---- verification --------

        val diff = originPath.diff(nodePath)

        assertEquals(1, diff.size)
        assertEquals("meta.json", diff.first().name)
        assertEquals(DiffType.CONTENT_DIFFERENT, diff.first().type)
    }
}