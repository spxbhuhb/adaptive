package `fun`.adaptive.grove.doc.server

import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.grove.doc.lib.compiler.GroveDocCompilation
import `fun`.adaptive.grove.doc.lib.compiler.GroveDocCompiler
import `fun`.adaptive.grove.doc.model.GroveDocFileEvent
import `fun`.adaptive.value.AvValueWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.io.files.Path

class GroveDocWorker : WorkerImpl<GroveDocWorker>() {

    val values by workerImpl<AvValueWorker>()

    val inPath by setting<Path> { "GROVE_DOC_IN_PATH" }
    val mdOutPath by setting<Path> { "GROVE_DOC_MD_OUT_PATH" }

    var watcher : AutoCloseable? = null

    override suspend fun run() {

        val compilation = GroveDocCompilation(inPath, mdOutPath, values)

        val channel = Channel<GroveDocFileEvent>(10)
        watcher = fileWatcher(inPath, channel)

        GroveDocCompiler(compilation).apply {
            continuousCompile(channel)
        }
    }

    override fun unmount() {
        watcher?.close()
        super.unmount()
    }

}