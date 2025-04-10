package `fun`.adaptive.auto.test.collection.update

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class CollectionUpdateTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")

    val content_empty = listOf<TestData>()
    val content_12 = listOf(td12)
    val content_23 = listOf(td23)
    val content_12_23 = listOf(td12, td23)

    @Test
    fun update_through_adat_store() {
        val instance = autoCollectionOrigin(content_12)

        val item = instance.value.first()

        val updated = item.update(item::i to 23, item::s to "b")

        assertEquals(td23, instance.value.first())
        assertEquals(td23, updated)
    }
}