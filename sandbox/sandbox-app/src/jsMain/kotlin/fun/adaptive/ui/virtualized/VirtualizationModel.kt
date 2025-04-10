package `fun`.adaptive.ui.virtualized

class VirtualizationModel<T>(
    val items: List<T>
) {
    var virtualOffset: Double = 0.0
    var virtualScrollTop: Double = 0.0
}