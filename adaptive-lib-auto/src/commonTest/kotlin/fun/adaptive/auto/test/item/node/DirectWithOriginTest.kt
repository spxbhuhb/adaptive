package `fun`.adaptive.auto.test.item.node

import `fun`.adaptive.auto.api.autoItemNode
import `fun`.adaptive.auto.api.autoItemOrigin
import `fun`.adaptive.auto.test.support.TestData
import kotlin.test.Test
import kotlin.test.assertEquals

class DirectWithOriginTest {

    @Test
    fun basic() {
        val td = TestData(12, "a")
        val origin = autoItemOrigin(td)
        val node = autoItemNode(origin)

        while (node.time.timestamp != origin.time.timestamp) {
            continue
        }

        origin.update(td::i to 23)
        assertEquals(origin.time.timestamp, node.time.timestamp)
        assertEquals(23, node.value.i)

        node.update(td::i to 34)
        assertEquals(node.time.timestamp, origin.time.timestamp)
        assertEquals(34, origin.value.i)
    }

}