package `fun`.adaptive.doc.example.codefence

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.instruction.dp

/**
 * # Code fence - scrolling
 *
 * Code fence with a long content and sizes set so that it has
 * to scroll vertically.
 *
 * The codeFence recognizes if the content is larger than the
 * available space and automatically enables scrolling.
 */
@Adaptive
fun codeFenceScrollingExample(): AdaptiveFragment {

    codeFence(longCode) .. width { 400.dp } .. height { 200.dp }

    return fragment()
}

val longCode = """
// Example function to calculate the area of an ellipse
fun exampleEllipseArea(semiMajor: Double, semiMinor: Double): Double {
    return Math.PI * semiMajor * semiMinor
}

// Example function to print the result in a friendly format
fun printExampleArea(label: String, area: Double) {
    println("The example area of '${'$'}label' is ${'$'}{"%.2f".format(area)}")
}

fun main() {
    // Sample input values for demonstration
    val exampleLabel1 = "Test Ellipse A"
    val exampleA1 = 5.0  // semi-major axis
    val exampleB1 = 3.0  // semi-minor axis

    val exampleLabel2 = "Test Ellipse B"
    val exampleA2 = 7.5
    val exampleB2 = 4.2

    // Perform example calculations
    val area1 = exampleEllipseArea(exampleA1, exampleB1)
    val area2 = exampleEllipseArea(exampleA2, exampleB2)

    // Display results
    printExampleArea(exampleLabel1, area1)
    printExampleArea(exampleLabel2, area2)
}
""".trimIndent()