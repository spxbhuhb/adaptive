package `fun`.adaptive.iot.ui

import `fun`.adaptive.iot.space.addSpace
import `fun`.adaptive.iot.space.addSpaceTop
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.TestSupport.Companion.aioValueTest
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.utility.waitForReal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class AioUiTreeTest {

    @Test
    fun basic() = aioValueTest {

        var tops = emptyList<TreeItem<AioValueId>>()

        fun refreshTop(new: List<TreeItem<AioValueId>>) {
            tops = new
        }

        val tree = AioUiTree(
            clientBackend,
            clientTransport,
            clientBackend.scope,
            SpaceMarkers.SPACE,
            SpaceMarkers.SUB_SPACES,
            SpaceMarkers.TOP_SPACES,
            ::refreshTop
        )

        tree.start()

        val spaceId = addSpace(serverWorker, "Site 1", 1, SpaceMarkers.SITE)

        addSpaceTop(serverWorker, spaceId)

        waitForReal(1.seconds) { tree.topSpacesSize > 0 }

        assertEquals(1, tree.size)
        assertEquals(1, tops.size)
        assertEquals(spaceId, tops.first().data)

        tree.stop()
    }

}