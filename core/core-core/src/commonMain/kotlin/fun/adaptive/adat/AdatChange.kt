package `fun`.adaptive.adat

class AdatChange(
    // TODO clean up List vs Array in AdatChange (see CopyStore update)
    val path: List<String>,
    val value: Any?
) {
    fun next() = AdatChange(path.drop(1), value)
}