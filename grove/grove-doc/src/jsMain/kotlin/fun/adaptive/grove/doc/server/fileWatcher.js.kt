package `fun`.adaptive.grove.doc.server

import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.grove.doc.model.GroveDocFileEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.io.files.Path

actual fun fileWatcher(rootPath: Path, channel: Channel<GroveDocFileEvent>) : AutoCloseable {
    unsupported("File watcher is not supported on JS platform")
}