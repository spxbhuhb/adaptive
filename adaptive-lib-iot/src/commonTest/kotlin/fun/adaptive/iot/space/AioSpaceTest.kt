package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.AioItem
import `fun`.adaptive.iot.item.AmvItemIdList
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.log.getLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AioSpaceTest {

    @Test
    fun `adding a space`() = test { worker ->
        val spaceId: AioValueId = addSpace(worker, "Building 1", 1, SpaceMarkers.BUILDING)

        val space = worker.item(spaceId)
        assertNotNull(space)

        assertNotNull(space[SpaceMarkers.SPACE])
        assertTrue(SpaceMarkers.BUILDING in space.markers)

        val spaceFromQuery = worker.query { it is AioItem && SpaceMarkers.BUILDING in it.markers }.firstOrNull() as? AioItem
        assertEquals(space, spaceFromQuery)

        val spaceAvmId = space.markers[SpaceMarkers.SPACE]
        assertNotNull(spaceAvmId)

        val spaceSpec = worker[spaceAvmId] as? AmvSpace
        assertNotNull(spaceSpec)

        assertEquals(12.3, spaceSpec.area)
    }


    @Test
    fun `adding a sub space`() = test { worker ->
        val spaceId = addSpace(worker, "Building 1", 1, SpaceMarkers.BUILDING)
        val subSpaceId = addSpace(worker, "Floor 1", 2, SpaceMarkers.FLOOR)

        addSubSpace(worker, spaceId, subSpaceId)

        val space = worker.item(spaceId)
        val subSpaceListId = space[SpaceMarkers.SUB_SPACES]
        checkNotNull(subSpaceListId)

        val subSpaceList = worker[subSpaceListId] as? AmvItemIdList
        assertNotNull(subSpaceList)

        val subSpace = worker.item(subSpaceId)
        assertNotNull(subSpace)
        assertEquals(subSpaceList.itemIds.first(), subSpace.uuid)

        val subSpace2Id = addSpace(worker, "Floor 2", 3, SpaceMarkers.FLOOR)

        addSubSpace(worker, spaceId, subSpace2Id)

        val subSpace2List = worker[subSpaceListId] as AmvItemIdList
        assertEquals(subSpace2List.itemIds.first(), subSpace.uuid)
        assertEquals(subSpace2List.itemIds[1], subSpace2Id)
    }

    fun test(timeout: Duration = 10.seconds, testFun: suspend (worker: AioValueWorker) -> Unit) =
        runTest(timeout = timeout) {
            val worker = AioValueWorker()
            worker.logger = getLogger("worker")

            val scope = CoroutineScope(Dispatchers.Unconfined)

            scope.launch {
                worker.run()
            }

            testFun(worker)

            worker.close()
        }
}
