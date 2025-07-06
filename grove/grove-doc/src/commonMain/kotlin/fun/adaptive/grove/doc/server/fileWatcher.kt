package `fun`.adaptive.grove.doc.server

import `fun`.adaptive.grove.doc.model.GroveDocFileEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.io.files.Path

expect fun fileWatcher(rootPath: Path, channel : Channel<GroveDocFileEvent>) : AutoCloseable