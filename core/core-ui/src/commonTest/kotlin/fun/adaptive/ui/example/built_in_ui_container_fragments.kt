package `fun`.adaptive.ui.example

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adaptive
fun boxExample() {
    box {
        box { // box in a box, size and positioning provided with an inner instruction
            frame(0.dp, 1.dp, 100.dp, 200.dp) // top 0.dp, left 1.dp, width 100.dp, height 200.dp
        }

        text("b") .. position(10.dp, 20.dp) // top 10.dp, left 20.dp, intrinsic size

        text("c") .. alignSelf.center // align to the center vertically and horizontally, intrinsic size
    }
}

@Adaptive
fun flowBoxExample() {
    flowBox {
        width { 100.dp } // set the width of the box, so it will wrap

        box { width { 60.dp } } // first row, the remaining width is 40.dp
        box { width { 30.dp } } // first row, the remaining width is 10.dp
        box { width { 20.dp } } // second row, remaining width is 90.dp
        box { width { 100.dp } } // third row, no remaining width
        box { width { 50.dp } } // fourth row, the remaining width is 50.dp
    }
}

@Adaptive
fun flowBoxWithLimitExample() {
    flowBox {
        // no width instruction, so the box will grow as much as possible
        flowItemLimit { 3 }

        text("a") // first row
        text("b") // first row
        text("c") // first row, reached the limit for one row
        text("d") // second row, even if there is enough space
    }
}

@Adaptive
fun rootBoxExample() {
    rootBox {
        text("Hello World!") .. position(0.dp, 0.dp) // places the text to the top-most corner root container
    }
}

@Adaptive
fun rowExample() {
    row { // puts "b" to the right of "a" horizontally
        text("a")
        text("b")
    }
}

@Adaptive
fun rowGapExample() {
    row {
        gap { 16.dp } // instructs the row to use a gap of 16.dp between the items

        text("a")
        text("b")
    }
}

@Adaptive
fun rowSpaceBetweenExample() {
    row {
        width { 200.dp } // specifies the width of the row as 200.dp
        spaceBetween // instructs the row to put the space between the children

        // As the width is 200.dp and the children's widths sum to 60.dp, the
        // remaining space is 200.dp - 60.dp = 140.dp. This is divided by
        // two as there are three children, so the gap between the children
        // will be 70.dp.

        text("a") .. width { 20.dp } // top 0.dp, left 0.dp
        text("b") .. width { 30.dp } // top 0.dp, left 90.dp (width of "a" + 70.dp)
        text("c") .. width { 10.dp } // top 0.dp, left 190.dp (left of "b" + width of "b" + 70.dp)
    }
}

@Adaptive
fun rowSpaceAroundExample() {
    row {
        width { 200.dp } // specifies the width of the row as 200.dp
        spaceAround // instructs the row to put the space between and around the children

        // As the width is 200.dp and the children's widths sum to 60.dp, the
        // remaining space is 200.dp - 60.dp = 140.dp. This is divided by
        // four as there are three children, so the space between and around
        // the children will be 35.dp.

        text("a") .. width { 20.dp } // top 0.dp, left 35.dp
        text("b") .. width { 30.dp } // top 0.dp, left 90.dp (left of "a" + width of "a" + 35.dp)
        text("c") .. width { 10.dp } // top 0.dp, left 155.dp (left of "b" + width of "b" + 35.dp)
    }
}

@Adaptive
fun columnExample() {
    row { // puts the "b" under "a"
        text("a")
        text("b")
    }
}

@Adaptive
fun columnGapExample() {
    row {
        gap { 16.dp } // instructs the column to use a gap of 16.dp between the items

        text("a")
        text("b")
    }
}

@Adaptive
fun columnSpaceBetweenExample() {
    column {
        height { 200.dp } // specifies the height of the column as 200.dp
        spaceBetween // instructs the column to put the space between the children

        // As the height is 200.dp and the children's heights sum to 60.dp, the
        // remaining space is 200.dp - 60.dp = 140.dp. This is divided by
        // two as there are three children, so the gap between the children
        // will be 70.dp.

        text("a") .. width { 20.dp } // top 0.dp, left 0.dp
        text("b") .. width { 30.dp } // top 90.dp (height of "a" + 70.dp), left 0.dp
        text("c") .. width { 10.dp } // top 190.dp (top of "b" + height of "b" + 70.dp), left 190.dp
    }
}

@Adaptive
fun columnSpaceAroundExample() {
    row {
        width { 200.dp } // specifies the height of the column as 200.dp
        spaceAround // instructs the column to put the space between and around the children

        // As the height is 200.dp and the children's heights sum to 60.dp, the
        // remaining space is 200.dp - 60.dp = 140.dp. This is divided by
        // four as there are three children, so the space between and around
        // the children will be 35.dp.

        text("a") .. width { 20.dp } // top 35.dp, left 0.dp
        text("b") .. width { 30.dp } // top 90.dp (top of "a" + height of "a" + 35.dp), left 0.dp
        text("c") .. width { 10.dp } // top 155.dp (top of "b" + height of "b" + 35.dp), left 0.p
    }
}

@Adaptive
fun gridExample() {
    grid {
        colTemplate(10.dp, 20.dp)
        rowTemplate(30.dp, 40.dp)

        text("a") // first row, first column, width 10.dp, height 30.dp
        text("b") // first row, second column, width 20.dp, height 30.dp
        text("c") // second row, first column, width 10.dp, height 40.dp
        text("d") // second row, second column, width 20.dp, height 40.dp
    }
}

@Adaptive
fun gridFrExample() {
    grid {
        // we use `fr` for column sizing, so we need a width
        width { 200.dp }

        // This colTemplate defines two fixed width and two fractional width columns.
        // The space available for fractional width columns is 120.dp (200.dp - width
        // of fixed width columns). The space available for fractional columns is
        // divided between the second and fourth column with a 7:5 ratio.

        colTemplate(15.dp, 7.fr, 65.dp, 5.fr)

        // there is a single row of 30.dp height

        rowTemplate(30.dp)

        text("a") // first row, first column, width 15.dp, height 30.dp, top 0.dp, left 0.dp
        text("b") // first row, second column, width 70.dp, height 30.dp, top 0.dp, left 15.dp
        text("c") // first row, third column, width 65.dp, height 30.dp, top 0.dp, left 85.dp
        text("d") // first row, fourth column, width 50.dp, height 30.dp, top 0.dp, left 150.dp
    }
}

@Adaptive
fun gridExtendExample() {
    grid {
        colTemplate(40.dp, 40.dp)
        rowTemplate(extend = 20.dp)

        text("a") // first row, first column, width 40.dp, height 20.dp
        text("b") // first row, second column, width 40.dp, height 20.dp
        text("c") // second row, first column, width 40.dp, height 20.dp
        text("d") // second row, second column, width 40.dp, height 20.dp
    }
}

@Adaptive
fun gridPlacingExample() {
    grid {
        colTemplate(32.dp, 1.fr, 32.dp, 1.fr, 32.dp) .. rowTemplate(50.dp)

        row(2.gridCol) {
            text("a")
        }

        row(4.gridCol) {
            text("b")
        }

    }
}