package `fun`.adaptive.lib.util.path

import `fun`.adaptive.file.absolute
import `fun`.adaptive.file.list
import `fun`.adaptive.file.read
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * Calculate differences between two directories (recursively).
 *
 * Reads and compares file content so this is an expensive operation.
 *
 * Not meant for big files as it reads the content at once.
 */
fun Path.diff(other: Path): List<PathDiffEntry> {
    val result = mutableListOf<PathDiffEntry>()

    val set1 = mutableSetOf<FileEntry>()
    listRecursively(this.absolute().toString(), this, set1)

    val set2 = mutableSetOf<FileEntry>()
    listRecursively(other.absolute().toString(), other, set2)

    for (file1 in set1) {
        val file2 = set2.find { it.relativePath == file1.relativePath }
        when {
            file2 == null -> {
                result.add(PathDiffEntry(PathDiffType.MISSING_FROM_2, file1.relativePath))
            }

            file1.isDirectory != file2.isDirectory -> {
                result.add(PathDiffEntry(PathDiffType.CONTENT_DIFFERENT, file1.relativePath))
            }

            file1.isDirectory -> {
                continue
            }

            ! file1.path.read().contentEquals(file2.path.read()) -> {
                result.add(PathDiffEntry(PathDiffType.CONTENT_DIFFERENT, file1.relativePath))
            }
        }
    }

    for (file2 in set2) {
        val file1 = set1.find { it.relativePath == file2.relativePath }
        if (file1 == null) {
            result.add(PathDiffEntry(PathDiffType.MISSING_FROM_1, file2.relativePath))
        }
    }

    return result
}

private class FileEntry(
    val path: Path,
    val relativePath: String,
    val isDirectory: Boolean
) {
    override fun equals(other: Any?): Boolean {
        return relativePath == (other as? FileEntry)?.relativePath && other.isDirectory == isDirectory
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + relativePath.hashCode()
        result = 31 * result + isDirectory.hashCode()
        return result
    }
}

private fun listRecursively(root: String, current: Path, paths: MutableSet<FileEntry>) {

    val relativePath = current.absolute().toString().removePrefix(root).trimStart('/', '\\')

    val meta = SystemFileSystem.metadataOrNull(current)
    checkNotNull(meta)

    if (relativePath.isNotEmpty()) {
        paths.add(FileEntry(current, relativePath, meta.isDirectory))
    }

    if (meta.isDirectory) {
        current.list().forEach { listRecursively(root, it, paths) }
    }
}