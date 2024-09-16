package `fun`.adaptive.cookbook.auto

import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.backend.query.firstImpl
import `fun`.adaptive.cookbook.shared.ClientServerRecipe
import `fun`.adaptive.utility.waitFor
import kotlin.time.Duration.Companion.seconds

abstract class AutoRecipe : ClientServerRecipe() {

    final override suspend fun clientMain() {
        waitForServerInit()
        autoClientMain()
        waitForSync()
    }

    abstract suspend fun autoClientMain()

    /**
     *  Wait for the server side to initialization. Typically, we don't wait this way,
     *  but for the cookbook recipe it is needed so the server side initializes
     *  properly.
     */
    suspend fun waitForServerInit() {
        val serverAutoWorker = serverBackend.firstImpl<AutoWorker>()
        waitFor(1.seconds) { serverAutoWorker.backends.isNotEmpty() }
    }

    /**
     * Wait for sync between the client and the server side. Typically, we don't wait this way,
     * but for the cookbook recipe it is needed so the program has time to send the data and
     * write out the file.
     */
    suspend fun waitForSync() {
        val serverAutoWorker = serverBackend.firstImpl<AutoWorker>()
        val clientAutoWorker = clientBackend.firstImpl<AutoWorker>()

        val serverContext = serverAutoWorker.backends.values.first().context

        waitFor(2.seconds) {
            var synced : Boolean = true
            for (clientBackend in clientAutoWorker.backends.values) {
                synced = (synced && serverContext.time.timestamp == clientBackend.context.time.timestamp)
            }
            synced
        }
    }

}