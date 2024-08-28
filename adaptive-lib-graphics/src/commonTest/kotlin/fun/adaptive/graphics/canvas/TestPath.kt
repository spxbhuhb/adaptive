package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.graphics.canvas.platform.ActualPath
import `fun`.adaptive.graphics.svg.instruction.Arc

class TestPath(
    val canvas: TestCanvas
) : ActualPath {

    override fun moveTo(x: Double, y: Double) {
        canvas.trace += "M $x $y"
    }

    override fun lineTo(x: Double, y: Double) {
        canvas.trace += "L $x $y"
    }

    override fun closePath(x1: Double, y1: Double, x2: Double, y2: Double) {
        canvas.trace += "Z $x1 $y1 $x2 $y2"
    }

    override fun arcTo(arc: Arc) {
        canvas.trace += "A $arc.rx $arc.ry $arc.phi $arc.largeArc $arc.sweep $arc.x $arc.y"
    }

    override fun cubicCurve(x1: Double, y1: Double, x2: Double, y2: Double, x: Double, y: Double) {
        canvas.trace += "C $x1 $y1 $x2 $y2 $x $y"
    }

    override fun smoothCubicCurve(x2: Double, y2: Double, x: Double, y: Double) {
        canvas.trace += "S $x2 $y2 $x $y"
    }

    override fun quadraticCurve(x1: Double, y1: Double, x: Double, y: Double) {
        canvas.trace += "Q $x1 $y1 $x $y"
    }

    override fun smoothQuadraticCurve(x: Double, y: Double) {
        canvas.trace += "T $x $y"
    }

}