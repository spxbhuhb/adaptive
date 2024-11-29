package `fun`.adaptive.lib.util.path

import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.write
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DiffTest {

    @Test
    fun `both directories are empty`() {
        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        val diff = dir1.diff(dir2)

        assertTrue(diff.isEmpty())
    }

    @Test
    fun `1 contains a surplus empty directory`() {
        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        Path(dir2, "other").ensure()

        val diff = dir1.diff(dir2)

        assertEquals(1, diff.size)
        assertEquals("other", diff.first().name)
        assertEquals(DiffType.MISSING_FROM_1, diff.first().type)
    }

    @Test
    fun `2 contains a surplus empty directory`() {
        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        Path(dir1, "other").ensure()

        val diff = dir1.diff(dir2)

        assertTrue(diff.size == 1)
        assertEquals("other", diff.first().name)
        assertEquals(DiffType.MISSING_FROM_2, diff.first().type)
    }


    @Test
    fun `same directory structure but different file content`() {
        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        val file1 = Path(dir1, "file.txt")
        val file2 = Path(dir2, "file.txt")

        file1.write("content1")
        file2.write("content2")

        val diff = dir1.diff(dir2)

        assertTrue(diff.size == 1)
        assertEquals("file.txt", diff.first().name)
        assertEquals(DiffType.CONTENT_DIFFERENT, diff.first().type)
    }

    @Test
    fun `one directory has extra files and folders`() {
        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        val extraFile = Path(dir1, "extra_file.txt")
        Path(dir1, "extra_dir").ensure()
        extraFile.write("extra")

        val diff = dir1.diff(dir2)

        assertEquals(2, diff.size)
        assertTrue(diff.any { it.name == "extra_file.txt" && it.type == DiffType.MISSING_FROM_2 })
        assertTrue(diff.any { it.name == "extra_dir" && it.type == DiffType.MISSING_FROM_2 })
    }

    @Test
    fun `files with same name but differ in directory and file`() {
        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        Path(dir1, "same").ensure()
        Path(dir2, "same").apply { this.write("file content") }

        val diff = dir1.diff(dir2)

        assertEquals(1, diff.size)
        assertEquals("same", diff.first().name)
        assertEquals(DiffType.CONTENT_DIFFERENT, diff.first().type)
    }


}