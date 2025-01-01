package `fun`.adaptive.auto.test.collection.persistence

import `fun`.adaptive.auto.api.autoCommon
import `fun`.adaptive.auto.test.support.CollectionTestSetupDirect
import `fun`.adaptive.auto.test.support.content_12
import `fun`.adaptive.auto.test.support.td12
import `fun`.adaptive.auto.test.support.td23
import `fun`.adaptive.auto.test.support.wait
import `fun`.adaptive.lib.util.path.PathDiffType
import `fun`.adaptive.lib.util.path.diff
import `fun`.adaptive.utility.PlatformType
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.platformType
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectionNodeFilePersistenceTest {

    @Test
    fun basic() {
        if (platformType == PlatformType.JsBrowser) return

        autoCommon()

        with(CollectionTestSetupDirect(clearedTestPath(), content_12)) {

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
    fun empty_at_connect() {
        if (platformType == PlatformType.JsBrowser) return

        autoCommon()

        with(CollectionTestSetupDirect(clearedTestPath(), emptyList())) {
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
    fun update_during_connect() {
        if (platformType == PlatformType.JsBrowser) return

        autoCommon()

        with(CollectionTestSetupDirect(clearedTestPath(), content_12)) {

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
    fun update_after_connect() {
        if (platformType == PlatformType.JsBrowser) return

        autoCommon()

        with(CollectionTestSetupDirect(clearedTestPath(), content_12)) {

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


}