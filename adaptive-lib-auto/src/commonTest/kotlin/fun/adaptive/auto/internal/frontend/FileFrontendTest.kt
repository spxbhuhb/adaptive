package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.auto.PropertyTestSetup
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.testPath
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.wireformat.api.Json
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class FileFrontendTest {

    @Test
    fun fromOrigin() {
        testPath.ensure()

        val path = Path(testPath, "FileFrontendTest.fromOrigin.json")
        val testData = TestData(12, "ab")

        runTest {

            SystemFileSystem.delete(path, mustExist = false)

            with(PropertyTestSetup(testData)) {

                val f1 = AdatClassFrontend(b1, wireFormat, testData, itemId, null,).also { b1.frontend = it }
                b2.frontend = FileFrontend(b2, wireFormat, itemId, null, null, Json, path)

                connect()

                waitForReal(2.seconds) { SystemFileSystem.exists(path) }

                fun read() = FileFrontend.read<TestData>(path, Json).third

                assertEquals(testData, read())

                f1.modify("i", 23)

                assertEquals(TestData(23, "ab"), read())

                f1.modify("i", 34)

                assertEquals(TestData(34, "ab"), read())

                f1.modify("s", "bc")

                assertEquals(TestData(34, "bc"), read())

            }
        }
    }

}