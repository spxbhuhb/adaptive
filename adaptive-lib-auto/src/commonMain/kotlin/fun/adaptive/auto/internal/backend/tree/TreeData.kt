/*
 * License CC0 - http://creativecommons.org/publicdomain/zero/1.0/
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software to
 * the public domain worldwide. This software is distributed without
 * any warranty.
 *
 * This code a Kotlin port of: https://madebyevan.com/algos/crdt-mutable-tree-hierarchy/
 */

package `fun`.adaptive.auto.internal.backend.tree

import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.internal.backend.TreeBackend
import kotlin.collections.get
import kotlin.math.max

class TreeData(
    val backend : TreeBackend<*>,
) {

    val activeNodes = Node(ItemId(0, 1))
    val removedNodes = Node(ItemId(0, 2))

    val root = Node(ItemId(0, 0)).also { root ->
        root.children += activeNodes
        root.children += removedNodes
        activeNodes.edges[root.id] = 0
        removedNodes.edges[root.id] = 0
    }

    val nodes = mutableMapOf(
        root.id to root,
        activeNodes.id to activeNodes,
        removedNodes.id to removedNodes
    )

    /**
     * Keep the in-memory tree up to date and cycle-free as the database is mutated.
     */
    fun afterApply(itemId : ItemId, parentId: ItemId, counter : Int?, commit : Boolean = true) {
        // Each mutation takes place on the child. The key is the parent
        // identifier and the value is the counter for that graph edge.

        var child = nodes[itemId]
        if (child == null) {
            // Make sure the child exists
            child = Node(itemId)
            nodes[itemId] = child
        }

        if (parentId !in nodes) {
            // Make sure the parent exists
            nodes[parentId] = Node(parentId)
        }

        if (counter == null) {
            // Undo can revert a value back to undefined
            child.edges.remove(parentId)
        } else {
            // Otherwise, add an edge from the child to the parent
            child.edges[parentId] = counter
        }

        if (commit) {
            recomputeParentsAndChildren()
        }
    }

    fun recomputeParentsAndChildren() {
        // Start off with all children arrays empty and each parent pointer
        // for a given node set to the most recent edge for that node.
        for (node in nodes.values) {
            // Set the parent identifier to the link with the largest counter
            node.parent = nodes[edgeWithLargestCounter(node)]
            node.children.clear()
        }

        // At this point all nodes that can reach the root form a tree (by
        // construction, since each node other than the root has a single
        // parent). The parent pointers for the remaining nodes may form one
        // or more cycles. Gather all remaining nodes detached from the root.
        val nonRootedNodes = mutableSetOf<Node>()
        for (node in nodes.values) {
            if (! isNodeUnderOtherNode(node, this.root)) {
                var current: Node? = node
                while (current != null && current !in nonRootedNodes) {
                    nonRootedNodes.add(current)
                    current = current.parent
                }
            }
        }

        // Deterministically reattach these nodes to the tree under the root
        // node. The order of reattachment is arbitrary but needs to be based
        // only on information in the database so that all peers reattach
        // non-rooted nodes in the same order and end up with the same tree.
        if (nonRootedNodes.isNotEmpty()) {
            // All "ready" edges already have the parent connected to the root,
            // and all "deferred" edges have a parent not yet connected to the
            // root. Prioritize newer edges over older edges using the counter.
            val deferredEdges = mutableMapOf<Node, MutableList<Edge>>()
            val readyEdges = PriorityQueue()

            for (child in nonRootedNodes) {
                for ((parentID, counter) in child.edges) {
                    val parent = checkNotNull(this.nodes[parentID]) { "inconsistent tree, missing parent node" }
                    if (parent !in nonRootedNodes) {
                        readyEdges.push(Edge(parent, child, counter))
                    } else {
                        var edges = deferredEdges[parent]
                        if (edges == null) {
                            edges = mutableListOf()
                            deferredEdges[parent] = edges
                        }
                        edges += Edge(parent, child, counter)
                    }
                }
            }

            var top = readyEdges.pop()
            while (top != null) {
                // Skip nodes that have already been reattached
                val child = top.child

                if (child !in nonRootedNodes) {
                    top = readyEdges.pop()
                    continue
                }

                // Reattach this node
                child.parent = top.parent
                nonRootedNodes.remove(child)

                // Activate all deferred edges for this node
                readyEdges.push(deferredEdges[child])

                top = readyEdges.pop()
            }
        }

        // Add items as children of their parents so that the rest of the app
        // can easily traverse down the tree for drawing and hit-testing
        for (node in nodes.values) {
            val parent = node.parent ?: continue
            parent.children += node
        }

        // Sort each node's children by their identifiers so that all peers
        // display the same tree. In this demo, the ordering of siblings
        // under the same parent is considered unimportant. If this is
        // important for your app, you will need to use another CRDT in
        // combination with this CRDT to handle the ordering of siblings.
        for (node in nodes.values) {
            node.children.sort()
        }
    }

    fun addChildToParent(childID: NodeId, parentID: NodeId) {
        val edits = mutableListOf<Pair<Node, Node>>() // child to parent
        val child = checkNotNull(nodes[childID]) { "missing child node $childID" }
        val parent = checkNotNull(nodes[parentID]) { "missing parent node $parentID" }

        fun ensureNodeIsRooted(node: Node?) {
            var current = node
            while (current != null) {
                val p = current.parent ?: break
                val edge = edgeWithLargestCounter(current)
                if (edge !== p.id) edits += (current to p)
                current = p
            }
        }

        // Ensure that both the old and new parents remain where they are
        // in the tree after the edit we are about to make. Then move the
        // child from its old parent to its new parent.
        ensureNodeIsRooted(child.parent)
        ensureNodeIsRooted(parent)
        edits += (child to parent)

        // Apply all database edits accumulated above. If your database
        // supports syncing a set of changes in a single batch, then these
        // edits should all be part of the same batch for efficiency. The
        // order that these edits are made in shouldn't matter.
        for ((c, _) in edits) {
            var maxCounter = - 1
            for (counter in c.edges.values) {
                maxCounter = max(maxCounter, counter)
            }

            backend.moved(child.id, parent.id)
        }
    }

    /**
     * The edge with the largest counter is considered to be the most recent
     * one. If two edges are set simultaneously, the identifier breaks the tie.
     */
    private fun edgeWithLargestCounter(node: Node): NodeId? {
        var edgeId: NodeId? = null
        var largestCounter = - 1
        for ((id, counter) in node.edges) {
            if (counter > largestCounter || (counter == largestCounter && edgeId?.let { id > it } == true)) {
                edgeId = id
                largestCounter = counter
            }
        }
        return edgeId
    }

    /**
     * Returns true if and only if "node" is in the subtree under "other".
     * This function is safe to call in the presence of parent cycles.
     */
    private fun isNodeUnderOtherNode(node: Node, other: Node): Boolean {
        if (node === other) return true
        var tortoise: Node? = node
        var hare = node.parent
        while (hare != null && hare !== other) {
            if (tortoise === hare) return false // Cycle detected
            hare = hare.parent
            if (hare == null || hare === other) break
            tortoise = tortoise?.parent
            hare = hare.parent
        }
        return hare === other
    }
}
