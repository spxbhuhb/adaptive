import hu.simplexion.adaptive.designer.utility.hits
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.lib.sandbox.ui.misc.chessBoard
import hu.simplexion.adaptive.lib.sandbox.ui.misc.chessBoardSize
import hu.simplexion.adaptive.ui.common.fragment.box
import hu.simplexion.adaptive.ui.common.fragment.column
import hu.simplexion.adaptive.ui.common.fragment.grid
import hu.simplexion.adaptive.ui.common.fragment.layout.AbstractContainer
import hu.simplexion.adaptive.ui.common.fragment.text
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

@Adaptive
fun hitDetect() {
    var hits = listOf<AdaptiveFragment>()
    var x = 0.0
    var y = 0.0

    grid {
        maxSize
        rowTemplate((chessBoardSize * 40 + 2).dp, 1.fr)
        gapHeight { 24.dp }

        box {
            maxSize
            onClick {
                hits = hits(it.fragment as AbstractContainer<*, *>, it.x, it.y)
                x = it.x
                y = it.y
            }
            chessBoard()
        }

        column {
            maxSize .. scroll

            text("x: $x y: $y")
            for (hit in hits) {
                text(hit.instructions.firstOrNullIfInstance<Name>()?.name ?: "${hit::class.simpleName} @ ${hit.id}")
            }
        }
    }
}