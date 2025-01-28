package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.sheet.SheetViewModel

abstract class SheetOperation {

    abstract fun applyTo(viewModel: SheetViewModel)

}