package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.adat.encodeToPrettyJson
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.support.snapshot.FragmentSnapshot
import `fun`.adaptive.ui.support.snapshot.FragmentTestScript
import `fun`.adaptive.ui.support.snapshot.FragmentTestScriptStep
import `fun`.adaptive.ui.support.snapshot.snapshot
import `fun`.adaptive.ui.testing.uiTest
import `fun`.adaptive.utility.readString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class LoopTest {

    /**
     * Test loop layout updates:
     *
     * - start with an empty list
     * - add one item
     * - add another item
     * - change the last item
     */
    @Test
    fun basicLoop() = runTest(timeout = 1.hours) { // 1.hours for debugging

        val snapshots = Channel<FragmentSnapshot>(Channel.UNLIMITED)

        uiTest(0, 0, 600, 400) { adapter ->

            adapter.afterClosePatchBatch = { snapshots.trySend(it.snapshot()) }

            val testList = listOf<Int>()

            column {
                for (item in testList) {
                    text(item)
                }
            }

        }.also { adapter ->

            adapter.rootFragment.setStateVariable(1, listOf(1))
            adapter.rootFragment.setDirtyBatch(1)
            // first snapshot

            adapter.rootFragment.setStateVariable(1, listOf(1, 2))
            adapter.rootFragment.setDirtyBatch(1)
            // second snapshot

            adapter.rootFragment.setStateVariable(1, listOf(1, 3))
            adapter.rootFragment.setDirtyBatch(1)
            // third snapshot

            snapshots.close()

            val script = FragmentTestScript(snapshots.toList().map { FragmentTestScriptStep(it) }).encodeToPrettyJson()
            val expected = Path("./src/commonTest/kotlin/fun/adaptive/ui/fragment/structural/basicLoop.json").readString()

            assertEquals(expected, script)
        }

    }

}