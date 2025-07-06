package `fun`.adaptive.grove.doc.server

import `fun`.adaptive.grove.doc.model.GroveDocFileEvent
import `fun`.adaptive.grove.doc.model.GroveDocFileEventType
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.safeCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.nio.file.attribute.BasicFileAttributes
import kotlin.coroutines.coroutineContext
import kotlin.io.path.name

/**
 * A class that watches a directory structure and puts file paths into a Channel when certain files change.
 * It watches for changes in .md files under the 'doc' directory and *_example.kt files.
 * It ignores everything under directories with name 'build'.
 */
class GroveDocFileWatcher(
    private val rootPath: String,
    private val channel: Channel<GroveDocFileEvent>
) : AutoCloseable {
    private val logger = getLogger("GroveDocFileWatcher")

    private val watchService: WatchService = FileSystems.getDefault().newWatchService()
    private val dirToKeyMap = mutableMapOf<Path, WatchKey>()

    private val watcherScope = CoroutineScope(Dispatchers.Unconfined)

    fun run() {
        Thread { // FIXME I'm not sure about using a thread to launch watcher, however, watcher actually blocks by default
            watcherScope.launch {
                logger.info { "Starting file watcher for ${Paths.get(rootPath).toAbsolutePath().normalize()}" }
                registerAll(Paths.get(rootPath))
                try {
                    watchForChanges()
                } finally {
                    safeCall(logger) { channel.close() }
                    safeCall(logger) { close() }
                }
                logger.info { "File watcher for ${Paths.get(rootPath).normalize()} has stopped" }
            }
        }.start()
    }

    /**
     * Closes the watch service.
     */
    override fun close() {
        safeCall(logger) { watchService.close() }
        safeCall(logger) { watcherScope.cancel() }
    }

    /**
     * Registers all directories under the given directory with the watch service.
     * Ignores directories named 'build'.
     */
    private fun registerAll(start: Path) {
        var inDoc = false

        Files.walkFileTree(
            start,
            object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (inDoc) {
                        register(dir)
                        return FileVisitResult.CONTINUE
                    }

                    if (dir.name == "build") {
                        return FileVisitResult.SKIP_SUBTREE
                    }

                    if (dir.name == "doc" || dir.name == "kotlin") {
                        inDoc = true
                        register(dir)
                    }

                    return FileVisitResult.CONTINUE
                }

                override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                    if (dir.name == "doc" || dir.name == "kotlin") {
                        inDoc = false
                        register(dir)
                    }
                    return FileVisitResult.CONTINUE
                }
            }
        )
    }

    /**
     * Registers a single directory with the watch service.
     */
    private fun register(dir: Path) {
        val key = dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY)
        dirToKeyMap[dir] = key
    }

    /**
     * Returns a Flow that emits file paths when they change.
     * Only emits paths for .md files under the 'doc' directory and *_example.kt files.
     */
    suspend fun watchForChanges() {
        while (coroutineContext.isActive) {

            val key = watchService.take()
            val dir = dirToKeyMap.entries.find { it.value == key }?.key ?: continue

            for (event in key.pollEvents()) {
                val kind = event.kind()

                if (kind == StandardWatchEventKinds.OVERFLOW) continue

                @Suppress("UNCHECKED_CAST")
                val eventPath = (event as WatchEvent<Path>).context()
                val fullPath = dir.resolve(eventPath)

                if (Files.isDirectory(fullPath)) {
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        // If a new directory is created, register it
                        if (fullPath.name == "doc" || fullPath.name == "kotlin") {
                            registerAll(fullPath)
                        }
                    }
                } else {
                    // Check if the file matches our criteria
                    if (shouldWatchFile(fullPath)) {
                        when (kind) {
                            StandardWatchEventKinds.ENTRY_CREATE -> GroveDocFileEventType.Create
                            StandardWatchEventKinds.ENTRY_MODIFY -> GroveDocFileEventType.Modify
                            StandardWatchEventKinds.ENTRY_DELETE -> GroveDocFileEventType.Delete
                            else -> null
                        }?.let { type ->
                            channel.send(GroveDocFileEvent(type, path = fullPath.toString()))
                        }
                    }
                }
            }

            // Reset the key - if no longer valid, remove from map
            if (! key.reset()) {
                dirToKeyMap.entries.removeIf { it.value == key }
                if (dirToKeyMap.isEmpty()) {
                    break
                }
            }
        }
    }

    /**
     * Determines if a file should be watched based on its path.
     * Returns true for .md files under the 'doc' directory and *_example.kt files.
     */
    private fun shouldWatchFile(path: Path): Boolean {
        val pathString = path.toString()

        // Check if it's an .md file under the 'doc' directory
        if (pathString.contains(File.separator + "doc" + File.separator) &&
            pathString.endsWith(".md")
        ) {
            return true
        }

        // Check if it's a *_example.kt file
        if (pathString.endsWith("_example.kt")) {
            return true
        }

        return false
    }

}