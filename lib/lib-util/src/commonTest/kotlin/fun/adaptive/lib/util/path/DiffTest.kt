package `fun`.adaptive.lib.util.path

import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.file.clearedTestPath
import `fun`.adaptive.file.ensure
import `fun`.adaptive.file.write
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DiffTest {

    @Test
    fun both_directories_are_empty() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        val diff = dir1.diff(dir2)

        assertTrue(diff.isEmpty())
    }

    @Test
    fun first_contains_a_surplus_empty_directory() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        Path(dir2, "other").ensure()

        val diff = dir1.diff(dir2)

        assertEquals(1, diff.size)
        assertEquals("other", diff.first().name)
        assertEquals(PathDiffType.MISSING_FROM_1, diff.first().type)
    }

    @Test
    fun second_contains_a_surplus_empty_directory() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        Path(dir1, "other").ensure()

        val diff = dir1.diff(dir2)

        assertTrue(diff.size == 1)
        assertEquals("other", diff.first().name)
        assertEquals(PathDiffType.MISSING_FROM_2, diff.first().type)
    }


    @Test
    fun same_directory_structure_but_different_file_content() {
        if (GlobalRuntimeContext.platform.isJs) return

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
        assertEquals(PathDiffType.CONTENT_DIFFERENT, diff.first().type)
    }

    @Test
    fun one_directory_has_extra_files_and_folders() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        val extraFile = Path(dir1, "extra_file.txt")
        Path(dir1, "extra_dir").ensure()
        extraFile.write("extra")

        val diff = dir1.diff(dir2)

        assertEquals(2, diff.size)
        assertTrue(diff.any { it.name == "extra_file.txt" && it.type == PathDiffType.MISSING_FROM_2 })
        assertTrue(diff.any { it.name == "extra_dir" && it.type == PathDiffType.MISSING_FROM_2 })
    }

    @Test
    fun files_with_same_name_but_differ_in_directory_and_file() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testDir = clearedTestPath()
        val dir1 = testDir.ensure("dir1")
        val dir2 = testDir.ensure("dir2")

        Path(dir1, "same").ensure()
        Path(dir2, "same").apply { this.write("file content") }

        val diff = dir1.diff(dir2)

        assertEquals(1, diff.size)
        assertEquals("same", diff.first().name)
        assertEquals(PathDiffType.CONTENT_DIFFERENT, diff.first().type)
    }
}