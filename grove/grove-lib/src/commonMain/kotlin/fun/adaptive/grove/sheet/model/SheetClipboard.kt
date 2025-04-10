package `fun`.adaptive.grove.sheet.model

class SheetClipboard(
    val items : List<SheetClipboardItem>
) {
    fun isEmpty() = items.isEmpty()
}