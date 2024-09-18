package `fun`.adaptive.auto.backend

import `fun`.adaptive.auto.PropertyTestSetup
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyTest {

    @Test
    fun basic() {
        val testData = TestData(12, "ab")

        runTest {
            with(PropertyTestSetup(testData)) {

                val f1 = AdatClassFrontend(b1, TestData.adatWireFormat, testData, null, null).also { b1.frontend = it }
                val f2 = AdatClassFrontend(b2, TestData.adatWireFormat, null, null, null).also { b2.frontend = it }

                connect()

                while (f2.value == null) {
                    delay(10)
                }

                assertEquals(12, f2.value !!.i)

                f1.modify("i", 23)
                assertEquals(23, f2.value !!.i)

                f2.modify("i", 34)
                assertEquals(34, f1.value !!.i)

                f1.modify("s", "cd")
                assertEquals("cd", f2.value !!.s)

                f2.modify("s", "ef")
                assertEquals("ef", f1.value !!.s)

            }
        }
    }

}