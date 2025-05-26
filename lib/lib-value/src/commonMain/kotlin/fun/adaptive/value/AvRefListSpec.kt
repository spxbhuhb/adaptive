package `fun`.adaptive.value

import `fun`.adaptive.adat.Adat

@Adat
class AvRefListSpec(
    val refs: List<AvValueId> = emptyList()
) {
    companion object {
        fun refListOf(vararg refs: AvValue<*>) = AvRefListSpec(refs.map { it.uuid })
    }
}