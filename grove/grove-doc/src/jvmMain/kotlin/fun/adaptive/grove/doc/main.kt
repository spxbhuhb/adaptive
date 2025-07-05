package `fun`.adaptive.grove.doc

import `fun`.adaptive.grove.doc.lib.compiler.groveDocMain
import `fun`.adaptive.utility.waitFor
import kotlinx.coroutines.runBlocking
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.deleteRecursively
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalPathApi::class)
fun main(args: Array<String>) {

    val effectiveArgs = if (args.isEmpty()) {
        // when started from IntelliJ the path is the root of the repo

        // these are fine, paths are quite unique
        Path("./build/adaptive/doc").deleteRecursively()
        Path("./site/site-app/var/values").deleteRecursively()

        arrayOf(
            ".",
            "./build/adaptive/doc",
            "./site/site-app/var/values"
        )
    } else {
        args
    }

    groveDocMain(effectiveArgs).apply {
        runBlocking {
            waitFor(10.seconds) { valueWorker.isIdle }
        }
    }
}