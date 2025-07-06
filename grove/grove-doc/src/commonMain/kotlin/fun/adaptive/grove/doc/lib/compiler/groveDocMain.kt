package `fun`.adaptive.grove.doc.lib.compiler

import `fun`.adaptive.log.getLogger
import `fun`.adaptive.persistence.ensure
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.seconds

suspend fun groveDocMain(args: Array<String>) : GroveDocCompilation {
    check(args.size == 3) { "usage: <in> <md-out> <values-out>" }

    val inPath = Path(args[0])
    val mdOutPath = Path(args[1]).ensure()
    val valuesOutPath = Path(args[2]).ensure()

    val valueServer = embeddedValueServer(FilePersistence(valuesOutPath.ensure()))
    val valueWorker = valueServer.serverWorker

    val compilation = GroveDocCompilation(
        inPath,
        mdOutPath,
        valueWorker
    )

    GroveDocCompiler(compilation).compile()

    for (notification in compilation.notifications) {
        getLogger("GroveDocCompiler").warning(notification.message + "\n" + notification.paths.joinToString("\n") + "\n")
    }

    waitFor(10.seconds) { valueWorker.isIdle }

    return compilation
}