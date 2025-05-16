package `fun`.adaptive.lib.util.path

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.file.list
import kotlinx.io.files.Path

/**
 * Sort files into a multi-level directory structure based on the **END** digits of the UUID.
 *
 * Uses the end digits to make distribution random. With UUID version 7 the first digits are
 * basically the same.
 *
 * The number of levels should be chosen by the estimated maximum number of files to achieve
 * a balance between the number of directories and files (see explanation below).
 *
 * Example of a two level setup:
 *
 * ```text
 * 00
 *   56
 *     b4e58f45-9f59-4b8f-91a7-a4e0e9465600
 *   82
 *     48852c46-8e5a-40a1-a9c8-e757c6f58200
 * f7
 *   13
 *     5e38c5c1-9134-4528-be20-e1bdbf0e13f7
 * ```
 *
 * ## Recommended File Number - Level Pairs
 * (credit: ChatGPT, considering common file systems)
 *
 * When considering real-world file systems, we need to take into account:
 *
 * - Maximum number of files per directory (performance degrades after a certain threshold).
 * - Directory lookup efficiency (some file systems slow down with large directories).
 * - Scalability and balance between depth and breadth.
 *
 * | **Estimated File Count** | **Recommended Levels** | **Reasoning (File System Considerations)** |
 * |--------------------------|-----------------------|--------------------------------------------|
 * | 1,000                    | 0                     | Most modern file systems handle 1,000 files in a single directory efficiently. |
 * | 10,000                   | 1                     | Some file systems (e.g., ext4, NTFS) slow down with >10,000 files in one directory. |
 * | 100,000                  | 2                     | At this scale, directory lookup performance degrades on ext4, NTFS, APFS. |
 * | 1,000,000                | 2-3                   | NTFS and ext4 slow significantly beyond 100,000 files in a single directory. |
 * | 10,000,000               | 3-4                   | Large-scale file storage benefits from deeper hierarchy for performance. |
 * | 100,000,000              | 4-5                   | Necessary for avoiding performance bottlenecks in large repositories. |
 *
 * **File System-Specific Notes**
 *
 * - **ext4 (Linux):** Can handle millions of files but benefits from hierarchical directories beyond **10,000** per directory.
 * - **NTFS (Windows):** Works well up to **100,000** files per directory but slows beyond that.
 * - **APFS (MacOS):** Handles many files well but benefits from deeper nesting for millions of files.
 * - **XFS (High-performance Linux FS):** Better for large numbers of files but still benefits from 3-4 levels at >1 million files.
 *
 * @property  levels     The number of levels to use.
 * @property  dirStore   When true, [pathFor] adds the UUID to the returned path.
 */
abstract class UuidFileStore<T>(
    val root : Path,
    val levels : Int,
    val dirStore : Boolean = false
) {

    /**
     * Get the **DIRECTORY** path for a given UUID.
     */
    open fun pathFor(uuid: UUID<*>): Path {
        val s = uuid.toString().reversed()
        val dirs = 0.until(levels).map { s.substring(it * 2, it * 2 + 2).reversed() }.toTypedArray()
        return if (dirStore) {
            Path(root, *dirs, uuid.toString())
        } else {
            Path(root, *dirs)
        }
    }

    /**
     * Traverse through all the paths and call [loadPath] for each at the
     * appropriate level.
     *
     * - Skips all paths with a name that starts with a dot.
     * - Skips all paths with a name that is shorter than 36 characters.
     * - Stops if [loadPath] throws an exception.
     */
    open fun loadAll(context : T) {
        loadDir(root, 0, context)
    }

    open fun loadDir(dirPath : Path, dirLevel : Int, context: T) {
        for (path in dirPath.list()) {
            val name = path.name
            if (dirLevel == levels) {
                if (name.length < 36 || name.startsWith('.')) continue
                loadPath(path, context)
            } else {
                if (name.length != 2 || name.startsWith('.')) continue
                loadDir(path, dirLevel + 1, context)
            }
        }
    }

    abstract fun loadPath(path : Path, context: T)

}
