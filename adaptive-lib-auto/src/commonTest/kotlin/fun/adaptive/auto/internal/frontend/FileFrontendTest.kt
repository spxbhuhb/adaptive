package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.auto.PropertyTestSetup
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.utility.ensureTestPath
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.utility.waitForReal
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class FileFrontendTest {

    @BeforeTest
    fun setup() {
        ensureTestPath()
    }

    @Test
    fun fromOrigin() {
        val path = Path(testPath, "FileFrontendTest.fromOrigin.json")
        val testData = TestData(12, "ab")

        runTest {

            SystemFileSystem.delete(path, mustExist = false)

            with(PropertyTestSetup(testData)) {

                val f1 = AdatClassFrontend(b1, wireFormat, testData, null, null, null).also { b1.frontEnd = it }
                b2.frontEnd = FileFrontend(b2, wireFormat, null, null, null, json, path)

                connect()

                waitForReal(2.seconds) { SystemFileSystem.exists(path) }

                assertEquals(testData, json.read(path, TestData))

                f1.modify("i", 23)

                assertEquals(TestData(23, "ab"), json.read(path, TestData))

                f1.modify("i", 34)

                assertEquals(TestData(34, "ab"), json.read(path, TestData))

                f1.modify("s", "bc")

                assertEquals(TestData(34, "bc"), json.read(path, TestData))

            }
        }
    }

}