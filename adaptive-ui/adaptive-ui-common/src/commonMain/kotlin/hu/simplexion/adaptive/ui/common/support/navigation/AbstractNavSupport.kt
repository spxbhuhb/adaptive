/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.navigation

import hu.simplexion.adaptive.foundation.AdaptiveFragment

abstract class AbstractNavSupport {

    abstract var root: NavData

    val nodes = mutableListOf<NavNode>()

    fun consume(owner: AdaptiveFragment): String {
        println("consume.root $root")
        println("consume.nodes $nodes")

        val indexOfExisting = nodes.indexOfFirst { it.owner === owner }

        if (indexOfExisting != - 1) {
            val newNode = nodes[indexOfExisting].consumeNext()
            nodes[indexOfExisting] = newNode
            return newNode.last()
        }

        val parentNode = findParent(owner) // non-existing node, find parent node

        if (parentNode == null) {
            val newNode = NavNode(owner, root, 1)
            nodes += newNode
            return newNode.last()
        }

        val newData = parentNode.navData.sub(parentNode.consumed)
        val newNode = NavNode(owner, newData, if (newData.segments.isEmpty()) 0 else 1)
        nodes += newNode
        return if (newNode.consumed == 1) newNode.last() else ""
    }

    fun removeNode(owner: AdaptiveFragment) {
        nodes.removeAll { it.owner == owner }
    }

    fun findParent(owner: AdaptiveFragment): NavNode? {
        var parent = owner.parent
        while (parent != null) {
            val node = nodes.firstOrNull { it.owner == parent }
            if (node != null) return node
            parent = parent.parent
        }
        return null
    }

    open fun segmentChange(owner: AdaptiveFragment, segment: String?) {

    }

}