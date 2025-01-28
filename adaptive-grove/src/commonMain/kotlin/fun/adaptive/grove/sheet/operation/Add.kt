package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.utility.UUID

class Add(
    key : String,
    mapping : List<LfmMapping>
) : SheetOperation() {

    val fragment = LfmDescendant(UUID(), key, mapping)

    override fun applyTo(viewModel: SheetViewModel) {
        viewModel.fragments += fragment
    }

    override fun toString(): String = "Add -- ${fragment.key} -- ${fragment.uuid}"

}