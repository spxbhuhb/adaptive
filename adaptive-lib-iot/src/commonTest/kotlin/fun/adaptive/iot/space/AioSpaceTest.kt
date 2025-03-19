package `fun`.adaptive.iot.space

import `fun`.adaptive.iot.item.*
import `fun`.adaptive.iot.space.markers.AmvSpace
import `fun`.adaptive.iot.space.markers.SpaceMarkers
import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.iot.value.AioValueWorker
import `fun`.adaptive.iot.value.operation.AvoAdd
import `fun`.adaptive.iot.value.operation.AvoUpdate
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System.now
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AioSpaceTest {

    suspend fun addSpace(worker: AioValueWorker, name: String, friendlyItemId: FriendlyItemId, spaceType: AioMarker): AioValueId {
        val spaceUuid = uuid7<AioValue>()

        val spaceSpec = AmvSpace(owner = spaceUuid, area = 12.3)

        val space = AioItem(
            name,
            AioWsContext.WSIT_SPACE,
            spaceUuid,
            now(),
            AioStatus.OK,
            friendlyItemId,
            markersOrNull = mutableMapOf(
                SpaceMarkers.SPACE to spaceSpec.uuid,
                spaceType to null
            ),
            parentId = null
        )

        worker.addAll(space, spaceSpec)

        waitForReal(1.seconds) { worker.isIdle }

        return spaceUuid
    }

    @Test
    fun addSpace() = test { worker ->
        val spaceId = addSpace(worker, "Building 1", 1, SpaceMarkers.BUILDING)

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

    suspend fun addSubSpace(worker: AioValueWorker, spaceId: AioValueId, subSpaceId: AioValueId) {
        val space = worker.item(spaceId)

        val originalId = space[SpaceMarkers.SUB_SPACES]

        if (originalId == null) {

            val subSpacesSpec = AmvItemIdList(owner = space.uuid, markerName = SpaceMarkers.SUB_SPACES, listOf(subSpaceId))

            worker.transaction(
                listOf(
                    AvoUpdate(space.copyWith(subSpacesSpec)),
                    AvoAdd(subSpacesSpec)
                )
            )
        } else {

            val original = worker[originalId]
            check(original is AmvItemIdList) { "Expected AmvItemIdList, got $original" }

            worker.update(original.copy(itemIds = original.itemIds + subSpaceId))
        }

        waitForReal(1.seconds) { worker.isIdle }
    }

    @Test
    fun addSubSpace() = test { worker ->
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
