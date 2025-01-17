package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.utility.UUID

@Adat
class LfmDescendant(
    val uuid : UUID<LfmDescendant>,
    val key : String,
    val mapping : List<LfmMapping>
)