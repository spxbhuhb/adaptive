package `fun`.adaptive.auto.integration

import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.exists
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.utility.waitForReal
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

private var producedValue: List<TestData>? = null

class OriginFolderTest {

    @Test
    fun basic() {
        autoTest(port = 8085) { originAdapter, connectingAdapter ->

            producedValue = null

            val folderName = "OriginFolderTest.basic"
            val folderPath = Path(testPath, folderName)
            folderPath.ensureExistsAndEmpty()

            test(connectingAdapter) {

                val a = autoList(TestData) { getService<AutoTestApi>(connectingAdapter.transport).folder(folderName) }

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

    @Test
    fun initial() {
        val t1 = TestData(12, "ab")
        val t2 = TestData(23, "bc")
        val t3 = TestData(34, "cd")

        val folderName = "OriginFolderTest.initial"
        val folderPath = Path(testPath, folderName)

        folderPath.ensureExistsAndEmpty()

        write(Path(folderPath, "1.1.json"), ItemId(1, 1), t1)
        write(Path(folderPath, "1.2.json"), ItemId(1, 2), t2)
        write(Path(folderPath, "1.3.json"), ItemId(1, 3), t3)

        autoTest(port = 8086) { originAdapter, connectingAdapter ->

            producedValue = null

            test(connectingAdapter) {

                val a = autoList(TestData) { getService<AutoTestApi>(connectingAdapter.transport).folder(folderName) }

                if (a != null) {
                    producedValue = a
                }

            }

            waitForReal(2.seconds) { producedValue != null }

            val setup = TestSetup<FolderFrontend<TestData>, AdatClassListFrontend<TestData>>(originAdapter, connectingAdapter)

            with(setup) {
                assert(listOf(t1, t2, t3))
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

        val existing = mutableSetOf<Path>()

        for (index in expected.indices) {

            val pv = producedValue !![index]
            val pt = originFrontend.pathFor(originFrontend.itemId(pv), pv)

            assertEquals(expected[index], pv)
            assertTrue(pt.exists())
            assertEquals(expected[index], read(pt))

            existing += pt
        }

        assertEquals(existing, SystemFileSystem.list(originFrontend.path).filter { ! it.name.startsWith(".") }.toSet())
    }
}