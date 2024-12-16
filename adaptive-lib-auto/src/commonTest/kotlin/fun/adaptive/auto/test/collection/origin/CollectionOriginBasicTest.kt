package `fun`.adaptive.auto.test.collection.origin

import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class CollectionOriginBasicTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")

    val content_empty = listOf<TestData>()
    val content_12 = listOf(td12)
    val content_23 = listOf(td23)
    val content_12_23 = listOf(td12, td23)

    @Test
    fun createEmpty() {
        val instance = autoCollectionOrigin(content_empty)
        assertEquals(content_empty, instance.value)
        assertNotSame(content_empty, instance.value)
    }

    @Test
    fun createSingle() {
        val instance = autoCollectionOrigin(content_12)
        assertEquals(content_12, instance.value)
        assertNotSame(content_12, instance.value)

        assertNotSame(content_12.first(), instance.value.first())
    }

    @Test
    fun createMulti() {
        val instance = autoCollectionOrigin(content_12_23)
        assertEquals(content_12_23, instance.value)
        assertNotSame(content_12_23, instance.value)

        assertNotSame(content_12_23.first(), instance.value.first())
    }

    @Test
    fun addToEmpty() {
        val instance = autoCollectionOrigin(content_empty)

        instance += td12
        assertEquals(content_12, instance.value)

        instance += td23
        assertEquals(content_12_23, instance.value)
    }

    @Test
    fun add_to_single() {
        val instance = autoCollectionOrigin(content_12)

        instance += td23
        assertEquals(content_12_23, instance.value)
    }

    @Test
    fun add_and_remove() {
        val instance = autoCollectionOrigin(content_empty)

        instance += td12
        instance -= instance.value.first()

        assertEquals(content_empty, instance.value)
    }

    @Test
    fun add_two_remove_first() {
        val instance = autoCollectionOrigin(content_empty)

        instance += td12
        instance += td23
        instance -= instance.value.first { it == td12 }

        assertEquals(content_23, instance.value)
    }

    @Test
    fun add_two_remove_second() {
        val instance = autoCollectionOrigin(content_empty)

        instance += td12
        instance += td23
        instance -= instance.value.first { it == td23 }

        assertEquals(content_12, instance.value)
    }
}