package `fun`.adaptive.iot.space

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.item.AvRefList
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.value.AvValue.Companion.asAvItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AioSpaceTest {

    @Test
    @JsName("adding_a_space")
    fun `adding a space`() = test { worker ->
        val spaceId: AvValueId = addSpace(worker, "Building 1", "1", SpaceMarkers.BUILDING)

        val space = worker.item(spaceId).asAvItem<AioSpaceSpec>()
        assertNotNull(space)

        assertNotNull(SpaceMarkers.SPACE in space.markers)
        assertTrue(SpaceMarkers.BUILDING in space.markers)

        val spaceFromQuery = worker.query { it is AvValue<*> && SpaceMarkers.BUILDING in it.markers }.firstOrNull()?.asAvItem<AioSpaceSpec>()
        assertNotNull(spaceFromQuery)
        assertEquals(space, spaceFromQuery)

        assertEquals(12.3, spaceFromQuery.spec.area)
    }

    @Test
    @JsName("adding_a_sub_space")
    fun `adding a sub space`() = test { worker ->
        val spaceId = addSpace(worker, "Building 1", "1", SpaceMarkers.BUILDING)
        val subSpaceId = addSpace(worker, "Floor 1", "2", SpaceMarkers.FLOOR)

        addSubSpace(worker, spaceId, subSpaceId)

        val space = worker.item(spaceId)
        val subSpaceListId = space[SpaceMarkers.SUB_SPACES]
        checkNotNull(subSpaceListId)

        val subSpaceList = worker[subSpaceListId] as? AvRefList
        assertNotNull(subSpaceList)

        val subSpace = worker.item(subSpaceId)
        assertNotNull(subSpace)
        assertEquals(subSpaceList.refs.first(), subSpace.uuid)

        val subSpace2Id = addSpace(worker, "Floor 2", "3", SpaceMarkers.FLOOR)

        addSubSpace(worker, spaceId, subSpace2Id)

        val subSpace2List = worker[subSpaceListId] as AvRefList
        assertEquals(subSpace2List.refs.first(), subSpace.uuid)
        assertEquals(subSpace2List.refs[1], subSpace2Id)
    }

    fun test(timeout: Duration = 10.seconds, testFun: suspend (worker: AvValueWorker) -> Unit) =
        runTest(timeout = timeout) {
            val worker = AvValueWorker("general")
            worker.logger = getLogger("worker")

            val scope = CoroutineScope(Dispatchers.Unconfined)

            scope.launch {
                worker.mount()
                worker.run()
            }

            testFun(worker)
        }
}
