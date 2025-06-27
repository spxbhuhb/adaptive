package `fun`.adaptive.grove.sheet.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.model.SheetItem

@Adat
class SelectByIndex(
    val index: Int, // TODO replace with ItemIndex when Adat supports value classes
    override val additional: Boolean
) : Select() {

    override fun SheetViewBackend.findItems() : List<SheetItem> {
        return listOf(items[index])
    }

    override fun toString(): String =
        "Select by index $index $traceString"
}