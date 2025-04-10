package `fun`.adaptive.auto.test.collection.node

import `fun`.adaptive.auto.api.autoCollectionNode
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.test.support.TestData
import `fun`.adaptive.auto.test.support.wait
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DirectWithOriginTest {

    val td12 = TestData(12, "a")
    val td23 = TestData(23, "b")
    val td45 = TestData(45, "c")

    val content_empty = listOf<TestData>()
    val content_12 = listOf(td12)
    val content_23 = listOf(td23)
    val content_12_23 = listOf(td12, td23)
    val content_45_23 = listOf(td45, td23)

    @Test
    fun create_empty() {
        val origin = autoCollectionOrigin(content_empty)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        assertEquals(content_empty, node.value)
    }

    @Test
    fun create_single() = runTest {
        val origin = autoCollectionOrigin(content_12)
        val node = autoCollectionNode(origin)

        node.ensureValue()

        assertEquals(content_12, node.value)
    }

    @Test
    fun create_multi() {
        val origin = autoCollectionOrigin(content_12_23)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        assertEquals(content_12_23, node.value)
    }

    @Test
    fun add_to_empty_origin() {
        val origin = autoCollectionOrigin(content_empty)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        origin += td12

        wait(node, origin)

        assertEquals(content_12, node.value)
    }

    @Test
    fun add_to_empty_node() {
        val origin = autoCollectionOrigin(content_empty)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        node += td12

        wait(node, origin)

        assertEquals(content_12, origin.value)
    }

    @Test
    fun update_from_origin() {
        val origin = autoCollectionOrigin(content_12_23)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        origin.update(td12::i to 45, td12::s to "c") { it == td12 }

        wait(node, origin)

        assertEquals(content_45_23, node.value)
    }

    @Test
    fun update_from_node() {
        val origin = autoCollectionOrigin(content_12_23)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        node.update(td12::i to 45, td12::s to "c") { it == td12 }

        wait(node, origin)

        assertEquals(content_45_23, origin.value)
    }

    @Test
    fun remove_from_origin() {
        val origin = autoCollectionOrigin(content_12_23)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        origin -= origin.value.first { it == td12 }

        wait(node, origin)

        assertEquals(content_23, node.value)
    }

    @Test
    fun remove_from_node() {
        val origin = autoCollectionOrigin(content_12_23)
        val node = autoCollectionNode(origin)

        wait(node, origin)

        node -= origin.value.first { it == td12 }

        wait(node, origin)

        assertEquals(content_23, origin.value)
    }


}