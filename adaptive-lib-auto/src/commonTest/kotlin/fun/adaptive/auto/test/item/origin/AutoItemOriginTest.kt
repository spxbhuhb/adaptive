package `fun`.adaptive.auto.test.item.origin

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class AutoItemOriginTest {

    @Test
    fun create() {
        val td = TestData(12, "a")
        val instance = autoItemOrigin(td)
        assertEquals(td, instance.value)
        assertNotSame(td, instance.value)
    }
}