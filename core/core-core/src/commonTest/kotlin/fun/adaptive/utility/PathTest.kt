package `fun`.adaptive.utility

import `fun`.adaptive.file.clearedTestPath
import `fun`.adaptive.file.copy
import `fun`.adaptive.file.delete
import `fun`.adaptive.file.ensure
import `fun`.adaptive.file.equalsBySizeAndLastModification
import `fun`.adaptive.file.exists
import `fun`.adaptive.file.read
import `fun`.adaptive.file.resolve
import `fun`.adaptive.file.syncBySizeAndLastModification
import `fun`.adaptive.file.write
import `fun`.adaptive.runtime.GlobalRuntimeContext
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Duration.Companion.milliseconds

class PathTest {

    val testFqn = "fun.adaptive.utility.PathTest"

    @Test
    fun testCopyExistsNoOverride() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testPath = clearedTestPath("$testFqn.testCopyExistsNoOverride")
        val filePath = testPath.resolve("test.txt")

        filePath.write("Hello World!")

        assertFailsWith(IllegalStateException::class) {
            filePath.copy(filePath)
        }
    }

    @Test
    fun testCopyExistsOverride() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testPath = clearedTestPath("$testFqn.testCopyExistsOverride")
        val sourcePath = testPath.resolve("test.txt")
        val targetPath = testPath.resolve("test-copy.txt")

        val expected = "Hello World!".encodeToByteArray()

        sourcePath.write(expected)
        targetPath.write("to be overwritten")

        sourcePath.copy(targetPath, overwrite = true)

        val result = targetPath.read()

        assertContentEquals(expected, result)
    }

    @Test
    fun testCopyNotExists() {
        if (GlobalRuntimeContext.platform.isJs) return

        val testPath = clearedTestPath("$testFqn.testCopyNotExists")
        val sourcePath = testPath.resolve("test.txt")
        val targetPath = testPath.resolve("test-copy.txt")

        val expected = "Hello World!".encodeToByteArray()

        sourcePath.write(expected)
        sourcePath.copy(targetPath)

        val result = targetPath.read()

        assertContentEquals(expected, result)
    }

    @Test
    fun testCopyKeepModification() = runTest {
        if (GlobalRuntimeContext.platform.let { it.isJs || it.isIos }) return@runTest

        val testPath = clearedTestPath("$testFqn.testCopyKeepModification")

        val sourcePath = testPath.resolve("test.txt")
        val targetPath1 = testPath.resolve("test-copy-1.txt")
        val targetPath2 = testPath.resolve("test-copy-2.txt")

        sourcePath.write("Hello World!".encodeToByteArray())

        waitForReal(5.milliseconds) // so modification time will be different (FAT32 fails, but OK....)

        sourcePath.copy(targetPath1)

        assertFalse(sourcePath.equalsBySizeAndLastModification(targetPath1))

        sourcePath.copy(targetPath2, keepModified = true)

        assertTrue(sourcePath.equalsBySizeAndLastModification(targetPath2))
    }

    @Test
    fun testEqualsBySizeAndLastModification() = runTest {
        if (GlobalRuntimeContext.platform.let { it.isJs || it.isIos }) return@runTest

        val testDir = clearedTestPath("$testFqn.testEqualsBySizeAndLastModification")
        val otherFile = testDir.resolve("other.txt")
        val thisFile = testDir.resolve("this.txt")

        fun assertDifferent() {
            assertFalse(thisFile.equalsBySizeAndLastModification(otherFile))
        }

        assertDifferent() // this does not exist

        thisFile.write("Test Content")

        assertDifferent() // other does not exist

        assertTrue(thisFile.equalsBySizeAndLastModification(thisFile)) // compare this with this

        waitForReal(5.milliseconds) // so modification time will be different (FAT32 fails, but OK....)

        otherFile.write("Test Content")

        assertDifferent() // modification time should be different
    }

    @Test
    @OptIn(DangerousApi::class) // confined to test path by `clearedTestPath`
    fun testSyncBySizeAndLastModification() {
        if (GlobalRuntimeContext.platform.let { it.isJs || it.isIos }) return

        val testDir = clearedTestPath("$testFqn.testSyncBySizeAndLastModification")
        val sourceDir = testDir.resolve("source").ensure()
        val targetDir = testDir.resolve("target").ensure()

        // Setup source directory
        val sourceFile1 = sourceDir.resolve("file1.txt")
        val sourceFile2 = sourceDir.resolve("file2.txt")
        sourceFile1.write("File one content")
        sourceFile2.write("File two content")

        // Ensure initial sync works
        val syncResult1 = targetDir.syncBySizeAndLastModification(sourceDir)
        assertTrue(syncResult1, "Initial sync should modify the target directory")

        // Verify target directory contents
        val targetFile1 = targetDir.resolve("file1.txt")
        val targetFile2 = targetDir.resolve("file2.txt")
        assertTrue(targetFile1.exists(), "Target directory should contain file1.txt")
        assertTrue(targetFile2.exists(), "Target directory should contain file2.txt")

        // Modify source directory and validate sync
        sourceFile1.write("Updated file one content", overwrite = true, useTemporaryFile = true)
        val syncResult2 = targetDir.syncBySizeAndLastModification(sourceDir)
        assertTrue(syncResult2, "Sync should detect and apply changes to target directory")

        // Check removal behavior
        sourceFile2.delete()
        val syncResult3 = targetDir.syncBySizeAndLastModification(sourceDir, remove = true)
        assertTrue(syncResult3, "Sync should remove deleted files")
        assertFalse(targetFile2.exists(), "Target directory should no longer contain file2.txt")
    }

}