package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat

@Adat
class LfmFragment(
    val externalStateVariables : List<LfmExternalStateVariable>,
    val internalStateVariables : List<LfmInternalStateVariable>,
    val descendants : List<LfmDescendant>
) {
    companion object {
        val EMPTY = LfmFragment(emptyList(), emptyList(), emptyList())
    }
}