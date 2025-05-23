package `fun`.adaptive.grove.doc

import `fun`.adaptive.utility.waitFor
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>) {
    groveDocMain(args).apply {
        runBlocking {
            waitFor(10.seconds) { valueWorker.isIdle }
        }
    }
}