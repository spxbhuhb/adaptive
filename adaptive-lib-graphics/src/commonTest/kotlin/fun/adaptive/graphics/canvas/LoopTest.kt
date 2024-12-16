package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.graphics.canvas.api.canvas
import `fun`.adaptive.graphics.canvas.api.fillText
import kotlin.test.Test

class LoopTest {

    @Test
    fun basic() {
        test {
            fragment().adapter.fragmentFactory += arrayOf(CanvasFragmentFactory)
            canvas {
                for (i in 0..10) {
                    fillText(i.toDouble(), i.toDouble(), i.toString())
                }
            }
        }
    }
}