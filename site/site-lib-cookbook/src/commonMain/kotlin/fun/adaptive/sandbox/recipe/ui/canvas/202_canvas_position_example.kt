package `fun`.adaptive.sandbox.recipe.ui.canvas

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.fillText
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.borders
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * # Canvas positioning
 */
@Adaptive
fun canvasPositionExample() : AdaptiveFragment {
    val positions = positionTree(root, centerX = 200.0, centerY = 200.0, radiusIncrement = 70.0)

    box {
        size(402.dp, 402.dp) .. borders.outline

        canvas {
            maxSize

            for (item in positions) {
                fillText(item.second.first, item.second.second, item.first.value)
            }
        }
    }

    return fragment()
}

data class TreeNode<T>(val value: T, val children: List<TreeNode<T>> = listOf())

fun <T> positionTree(root: TreeNode<T>, centerX: Double, centerY: Double, radiusIncrement: Double): List<Pair<TreeNode<T>, Pair<Double, Double>>> {
    // Map to store the (x, y) position of each node
    val positions = mutableMapOf<TreeNode<T>, Pair<Double, Double>>()

    // Helper function to recursively position nodes
    fun positionNode(node: TreeNode<T>, depth: Int, angleStart: Double, angleEnd: Double) {
        // Calculate the radius based on the depth of the node
        val radius = depth * radiusIncrement

        // Find the number of children
        val numChildren = node.children.size

        // Calculate the angle increment for each child node
        val angleIncrement = if (numChildren > 0) (angleEnd - angleStart) / numChildren else 0.0

        // Position each child
        node.children.forEachIndexed { index, child ->
            // Calculate angle for this child
            val angle = angleStart + index * angleIncrement

            // Convert polar coordinates to Cartesian coordinates
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)

            // Save the position
            positions[child] = Pair(x, y)

            // Recurse for the child's children, spreading them around this child
            positionNode(child, depth + 1, angle - angleIncrement / 2, angle + angleIncrement / 2)
        }
    }

    // Position the root node at the center
    positions[root] = Pair(centerX, centerY)

    // Start positioning child nodes
    positionNode(root, depth = 1, angleStart = 0.0, angleEnd = 2 * PI)

    return positions.toList()
}

val root = TreeNode(
    "Concentrator", listOf(
        TreeNode(
            "TH-0001", listOf(
                TreeNode("SZ-0001"),
                TreeNode("SZ-0002")
            )
        ),
        TreeNode(
            "TH-0002", listOf(
                TreeNode("SZ-0003"),
                TreeNode("SZ-0004")
            )
        )
    )
)
