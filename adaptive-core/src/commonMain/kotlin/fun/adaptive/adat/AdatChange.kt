package `fun`.adaptive.adat

class AdatChange(
    val path: List<String>,
    val value: Any?
) {
    fun next() = AdatChange(path.drop(1), value)
}