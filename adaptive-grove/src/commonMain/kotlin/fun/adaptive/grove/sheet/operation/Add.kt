package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.grove.sheet.SheetViewModel
import `fun`.adaptive.utility.UUID

class Add(
    uuid : UUID<LfmDescendant>,
    key : String,
    mapping : List<LfmMapping>
) : SheetOperation() {

    val fragment = LfmDescendant(uuid, key, mapping)

    override fun commit(viewModel: SheetViewModel) : Boolean {
        viewModel.fragments += fragment
        viewModel.root += fragment
        return false
    }

    override fun revert(viewModel: SheetViewModel) {
        viewModel.fragments.remove { it.uuid == fragment.uuid }
        viewModel.root -= fragment
    }

    override fun toString(): String = "Add -- ${fragment.key} -- ${fragment.uuid}"

}