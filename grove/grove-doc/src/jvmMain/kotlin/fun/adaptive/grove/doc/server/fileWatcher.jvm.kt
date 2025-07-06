package `fun`.adaptive.grove.doc.server

import `fun`.adaptive.grove.doc.server.GroveDocFileWatcher
import `fun`.adaptive.grove.doc.model.GroveDocFileEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.io.files.Path

actual fun fileWatcher(rootPath: Path, channel: Channel<GroveDocFileEvent>) : AutoCloseable =
    GroveDocFileWatcher(rootPath.toString(), channel).also { it.run() }