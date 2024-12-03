package `fun`.adaptive.auto.test.collection.persistence

import `fun`.adaptive.auto.api.autoCollectionNode
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.internal.persistence.CollectionFilePersistence
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.auto.test.support.wait
import `fun`.adaptive.lib.util.path.PathDiffType
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

        with(TestSetup(clearedTestPath(), list_12)) {

            origin.add(td23)

            wait(origin, node)

            assertTrue(originMetaPath.exists())
            assertTrue(nodeMetaPath.exists())

            assertTrue(Path(originPath, "0.1.json").exists())
            assertTrue(Path(originPath, "0.2.json").exists())

            val diff = originPath.diff(nodePath)

            assertEquals(1, diff.size)
            assertEquals("meta.json", diff.first().name)
            assertEquals(PathDiffType.CONTENT_DIFFERENT, diff.first().type)
        }

    }

    @Test
    fun `empty at connect`() {
        autoCommon()

        with(TestSetup(clearedTestPath(), emptyList())) {
            wait(origin, node)

            assertTrue(originMetaPath.exists())
            assertTrue(nodeMetaPath.exists())

            val diff = originPath.diff(nodePath)

            assertEquals(1, diff.size)
            assertEquals("meta.json", diff.first().name)
            assertEquals(PathDiffType.CONTENT_DIFFERENT, diff.first().type)
        }

    }

    @Test
    fun `update during connect`() {
        autoCommon()

        with(TestSetup(clearedTestPath(), list_12)) {

            origin.update(td12::i to 23) { true }
            origin.add(td23)

            wait(origin, node)

            assertTrue(originMetaPath.exists())
            assertTrue(nodeMetaPath.exists())

            assertTrue(Path(originPath, "0.1.json").exists())
            assertTrue(Path(originPath, "0.4.json").exists())

            val diff = originPath.diff(nodePath)

            assertEquals(1, diff.size)
            assertEquals("meta.json", diff.first().name)
            assertEquals(PathDiffType.CONTENT_DIFFERENT, diff.first().type)
        }

    }

    @Test
    fun `update after connect`() {
        autoCommon()

        with(TestSetup(clearedTestPath(), list_12)) {

            wait(origin, node)

            assertTrue(originMetaPath.exists())
            assertTrue(nodeMetaPath.exists())

            origin.update(td12::i to 23) { true }

            assertTrue(Path(originPath, "0.1.json").exists())

            val diff = originPath.diff(nodePath)

            assertEquals(1, diff.size)
            assertEquals("meta.json", diff.first().name)
            assertEquals(PathDiffType.CONTENT_DIFFERENT, diff.first().type)
        }

    }

    class TestSetup(
        testDir: Path,
        initialValue: List<TestData>,
        trace: Boolean = false
    ) {
        val originPath = Path(testDir, "origin").ensure()
        val originMetaPath = Path(originPath, "meta.json")

        val originPersistence = CollectionFilePersistence<TestData>(originMetaPath, Json) { itemId, item ->
            Path(originPath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val origin = autoCollectionOrigin(initialValue, persistence = originPersistence, trace = trace)

        val nodePath = Path(testDir, "node").ensure()
        val nodeMetaPath = Path(nodePath, "meta.json")

        val nodePersistence = CollectionFilePersistence<TestData>(nodeMetaPath, Json) { itemId, item ->
            Path(nodePath, "${itemId.peerId}.${itemId.timestamp}.json")
        }

        val node = autoCollectionNode(origin, persistence = nodePersistence, trace = trace)
    }

}