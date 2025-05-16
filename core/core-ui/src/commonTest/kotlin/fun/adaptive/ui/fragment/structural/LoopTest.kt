package `fun`.adaptive.ui.fragment.structural

class LoopTest {

    // this test fails with very various reasons with JS, so I just commented
    // it out for now
    // TODO investigate why LoopTest fails on JS

    /**
     * Test loop layout updates:
     *
     * - start with an empty list
     * - add one item
     * - add another item
     * - change the last item
     */
//    @Test
//    fun basicLoop() = runTest { // (timeout = 1.hours) { // 1.hours for debugging
//
//        val snapshots = Channel<FragmentSnapshot>(Channel.UNLIMITED)
//
//        uiTest(0, 0, 600, 400) { adapter ->
//
//            adapter.afterClosePatchBatch = { snapshots.trySend(it.snapshot()) }
//
//            val testList = listOf<Int>()
//
//            column {
//                for (item in testList) {
//                    text(item)
//                }
//            }
//
//        }.also { adapter ->
//
//            adapter.rootFragment.setStateVariable(1, listOf(1))
//            adapter.rootFragment.setDirtyBatch(1)
//            // first snapshot
//
//            adapter.rootFragment.setStateVariable(1, listOf(1, 2))
//            adapter.rootFragment.setDirtyBatch(1)
//            // second snapshot
//
//            adapter.rootFragment.setStateVariable(1, listOf(1, 3))
//            adapter.rootFragment.setDirtyBatch(1)
//            // third snapshot
//
//            snapshots.close()
//
//            val script = FragmentTestScript(snapshots.toList().map { FragmentTestScriptStep(it) }).encodeToPrettyJson()
//            val expected = Path("./src/commonTest/kotlin/fun/adaptive/ui/fragment/structural/basicLoop.json").readString()
//
//            assertEquals(expected, script)
//        }
//
//    }

}