package `fun`.adaptive.lib.util.path

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.resolve
import `fun`.adaptive.utility.write
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UuidFileStoreTest {

    class TestUuidFileStore(path : Path, levels: Int) : UuidFileStore(path, levels) {
        val loadedFiles = mutableListOf<Path>()

        override fun loadFile(path: Path) {
            loadedFiles.add(path)
        }
    }

    @Test
    fun `test pathFor generates correct path`() {
        val root = clearedTestPath()

        val store = TestUuidFileStore(root, 2)
        val uuid = UUID<Any>("48852c46-8e5a-40a1-a9c8-e757c6f58200")

        val expectedPath = Path(root, "00", "82")
        val actualPath = store.pathFor(uuid)

        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun `test pathFor with different levels`() {
        val root = clearedTestPath()

        val store1 = TestUuidFileStore(root, 1)
        val store3 = TestUuidFileStore(root, 3)
        val uuid = UUID<Any>("48852c46-8e5a-40a1-a9c8-e757c6f58200")

        assertEquals(Path(root, "00"), store1.pathFor(uuid))
        assertEquals(Path(root, "00", "82", "f5"), store3.pathFor(uuid))
    }

    @Test
    fun `test loadAll loads all valid files`() {
        val testRoot = clearedTestPath()

        val store = TestUuidFileStore(testRoot, 2)

        val subDir = testRoot.resolve("00/82").ensure()
        val validFile = subDir.resolve("48852c46-8e5a-40a1-a9c8-e757c6f58200").also { it.write("content") }
        subDir.resolve("invalid_file.txt").write("invalid")

        store.loadAll()

        assertEquals(1, store.loadedFiles.size)
        assertEquals(validFile, store.loadedFiles[0])
    }

    @Test
    fun `test loadAll with different levels`() {
        val testRoot = clearedTestPath()

        val store1 = TestUuidFileStore(testRoot, 1)
        val store3 = TestUuidFileStore(testRoot, 3)

        val subDir1 = testRoot.resolve("00").ensure()
        subDir1.resolve("48852c46-8e5a-40a1-a9c8-e757c6f58200").write("content")

        val subDir3 = testRoot.resolve("00/82/f5").ensure()
        subDir3.resolve("48852c46-8e5a-40a1-a9c8-e757c6f58200").write("content")

        store1.loadAll()
        assertEquals(1, store1.loadedFiles.size)

        store3.loadAll()
        assertEquals(1, store3.loadedFiles.size)
    }

    @Test
    fun `test loadAll stops on exception`() {
        class FailingUuidFileStore(path : Path, levels: Int) : UuidFileStore(path, levels) {
            override fun loadFile(root: Path) {
                throw RuntimeException("Test Exception")
            }
        }

        val testRoot = clearedTestPath()
        val store = FailingUuidFileStore(testRoot, 2)

        val subDir = testRoot.resolve("00/82").ensure()
        subDir.resolve("48852c46-8e5a-40a1-a9c8-e757c6f58200").write("content")

        assertFailsWith<RuntimeException> { store.loadAll() }
    }

    @Test
    fun `test loadAll with empty directory`() {
        val testRoot = clearedTestPath()
        val store = TestUuidFileStore(testRoot, 2)

        store.loadAll()
        assertEquals(0, store.loadedFiles.size)
    }
}