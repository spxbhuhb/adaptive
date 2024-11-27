package `fun`.adaptive.auto.test.collection.origin

import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class CollectionOriginBasicTest {

    @Test
    fun create() {
        val td = TestData(12, "a")
        val instance = autoItemOrigin(td)
        assertEquals(td, instance.value)
        assertNotSame(td, instance.value)
    }

    @Test
    fun update() {
        val td = TestData(12, "a")
        val instance = autoItemOrigin(td)

        instance.update(td::i to 23)
        assertEquals(23, instance.value.i)

        instance.update(td::s to "b")
        assertEquals("b", instance.value.s)

        instance.update(td::s to "c", td::i to 34)
        assertEquals(34, instance.value.i)
        assertEquals("c", instance.value.s)
    }

}