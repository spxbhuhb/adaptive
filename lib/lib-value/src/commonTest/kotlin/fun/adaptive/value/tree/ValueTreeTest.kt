package `fun`.adaptive.value.tree

import `fun`.adaptive.value.AvRefListSpec.Companion.refListOf
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.TestSupport.Companion.valueTest
import kotlin.test.Test

class ValueTreeTest {

    val nodeMarker = "space"
    val childListMarker = "sub-spaces"

    val parentSpaceRefLabel = "parentSpace"
    val childListRefLabel = "subSpaces"

    @Test
    fun buildTree() = valueTest {

        val childListId = AvValueId()

        val topNode = AvValue(
            markersOrNull = setOf(nodeMarker),
            refsOrNull = mapOf(childListRefLabel to childListId),
            spec = "Top Node"
        )

        val childNode1 = AvValue(
            markersOrNull = setOf(nodeMarker),
            refsOrNull = mapOf(parentSpaceRefLabel to topNode.uuid),
            spec = "Child Node 1"
        )

        val childNode2 = AvValue(
            markersOrNull = setOf(nodeMarker),
            refsOrNull = mapOf(parentSpaceRefLabel to topNode.uuid),
            spec = "Child Node 2"
        )

        val topNodeChildren = AvValue(
            markersOrNull = setOf(childListMarker),
            spec = refListOf(childNode1, childNode2)
        )

        serverWorker.queueAddAll(
            topNode,
            childNode1,
            childNode2,
            topNodeChildren
        )

    }

}