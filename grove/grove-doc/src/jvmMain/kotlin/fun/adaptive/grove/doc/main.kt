package `fun`.adaptive.grove.doc

import `fun`.adaptive.grove.doc.lib.compiler.groveDocMain
import `fun`.adaptive.persistence.deleteRecursively
import `fun`.adaptive.utility.DangerousApi
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import kotlin.io.path.ExperimentalPathApi

@OptIn(ExperimentalPathApi::class, DangerousApi::class)
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

    runBlocking {
        groveDocMain(effectiveArgs)
    }
}