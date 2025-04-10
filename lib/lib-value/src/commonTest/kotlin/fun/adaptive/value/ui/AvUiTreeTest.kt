package `fun`.adaptive.value.ui

import `fun`.adaptive.value.TestSupport.Companion.avValueTest
import kotlin.test.Test

class AvUiTreeTest {

    @Test
    fun basic() = avValueTest {
// FIXME this was a test in `iot`, space is not in the `value` module, so a mock should be made
//        var tops = emptyList<TreeItem<AvValueId>>()
//
//        fun refreshTop(new: List<TreeItem<AvValueId>>) {
//            tops = new
//        }
//
//        val tree = AvUiTree(
//            clientBackend,
//            clientTransport,
//            clientBackend.scope,
//            SpaceMarkers.SPACE,
//            SpaceMarkers.SUB_SPACES,
//            SpaceMarkers.TOP_SPACES,
//            ::refreshTop
//        )
//
//        tree.start()
//
//        val spaceId = addSpace(serverWorker, "Site 1", 1, SpaceMarkers.SITE)
//
//        addSpaceTop(serverWorker, spaceId)
//
//        waitForReal(1.seconds) { tree.topItemsSize > 0 }
//
//        assertEquals(1, tree.size)
//        assertEquals(1, tops.size)
//        assertEquals(spaceId, tops.first().data)
//
//        tree.stop()
    }

}