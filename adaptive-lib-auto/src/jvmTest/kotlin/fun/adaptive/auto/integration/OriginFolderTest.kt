package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.getService
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.waitForReal
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

private var producedValue: List<TestData>? = null

class OriginFolderTest {

    @Test
    fun basic() {
        autoTest(port = 8085) { originAdapter, connectingAdapter ->

            val folderName = "OriginFolderTest.basic"

            test(connectingAdapter) {

                val a = autoList(TestData, trace = true) { getService<AutoTestApi>().folder(folderName) }

                if (a != null) {
                    producedValue = a
                }

            }

            waitForReal(2.seconds) { producedValue != null }

            val setup = TestSetup<FolderFrontend<TestData>, AdatClassListFrontend<TestData>>(originAdapter, connectingAdapter)

            val t1 = TestData(12, "ab")
            val t2 = TestData(23, "bc")
            val t3 = TestData(34, "cd")

            with(setup) {
                addOrigin(t1, listOf(t1))
                addOrigin(t2, listOf(t1, t2))
                addOrigin(t3, listOf(t1, t2, t3))

                removeOrigin(t2, listOf(t1, t3))
                removeOrigin(t1, listOf(t3))
                removeOrigin(t3, emptyList())
            }
        }
    }

    suspend fun TestSetup<FolderFrontend<TestData>, AdatClassListFrontend<TestData>>.addOrigin(t : TestData, expected : List<TestData>) {
        originFrontend.add(t)
        assert(expected)
    }

    suspend fun TestSetup<FolderFrontend<TestData>, AdatClassListFrontend<TestData>>.removeOrigin(t : TestData, expected : List<TestData>) {
        val itemId = originFrontend.itemId(producedValue!!.first { it == t })
        originFrontend.remove(itemId)
        assert(expected)
    }

    suspend fun TestSetup<FolderFrontend<TestData>, AdatClassListFrontend<TestData>>.assert(expected : List<TestData>) {
        waitForReal(2.seconds) { producedValue?.size == expected.size }
        for (et in expected.indices) {
            assert(expected[et], et)
        }
    }

    fun TestSetup<FolderFrontend<TestData>, AdatClassListFrontend<TestData>>.assert(expected : TestData, index : Int) {
        val at1 = producedValue!![index]
        val pt1 = originFrontend.pathFor(originFrontend.itemId(at1), at1)

        assertEquals(expected, at1)
        assertTrue(pt1.exists())
        assertEquals(expected, read(pt1))
    }
}