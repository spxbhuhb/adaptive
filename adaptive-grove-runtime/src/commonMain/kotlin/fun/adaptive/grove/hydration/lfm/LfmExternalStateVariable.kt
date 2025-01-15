package `fun`.adaptive.grove.hydration.lfm

import `fun`.adaptive.adat.Adat

@Adat
class LfmExternalStateVariable(
    val name : String,
    val signature : String,
    val defaultValue : LfmExpression
)